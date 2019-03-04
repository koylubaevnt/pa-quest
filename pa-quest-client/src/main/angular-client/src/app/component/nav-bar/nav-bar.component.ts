import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { TokenStorageService } from 'src/app/service/token-storage.service';
import { UserQuestService } from 'src/app/service/user-quest.service';
import { Observable } from 'rxjs';
import { trigger, state, style, transition, animate } from '@angular/animations';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css'],
  // animations: [
  //   trigger('collapse', [
  //     state('open', style({
  //       opacity: '1',
  //       display: 'block',
  //       transform: 'translate3d(0, 0, 0)'
  //     })),
  //     state('closed', style({
  //       opacity: '0',
  //       display: 'none',
  //       transform: 'translate3d(0, -100%, 0)'
  //     })),
  //     transition('closed => open', animate('200ms ease-in')),
  //     transition('open => closed', animate('100ms ease-out'))
  //   ])
  // ]
})
export class NavBarComponent implements OnInit {
  
  private roles: string[] = [];
  authorized: boolean = false;
  finished: Boolean;
  show: boolean = true;
  // collapse: string = "closed";

  constructor(private tokenStorageService: TokenStorageService, private authService: AuthenticationService, private userQuesrService: UserQuestService) { }

  ngOnInit() {
    if (this.tokenStorageService.getToken()) {
      this.roles = this.tokenStorageService.getRoles();
      this.authorized = true;
    }
    this.authService.isLoggedIn().subscribe(value => {
      if (value) {
        if (this.tokenStorageService.getToken()) {
          this.roles = this.tokenStorageService.getRoles();
          this.authorized = true; 
        }
      //} else {
        //this.tokenStorageService.signOut();
      }     
    });
    this.userQuesrService.oneQuestIsFinished$.subscribe(finished => {
      this.finished = finished;
    });
    //this.userQuesrService.isFinishQuest().subscribe(_ => _);
  }

  
  hasRole(...roleName: string[]): boolean {
    let result: boolean = this.roles.some(role => {
      let contain: boolean = roleName.some(innerRole => {
        if (role === innerRole.toLowerCase()) {
          return true;
        }
        return false;
      })
      return contain;
    });
    return result;
  }

  logout() {
    this.tokenStorageService.signOut();
    window.location.reload();
  }

  toggleCollapse() {
    this.show = !this.show;
    //this.collapse = this.collapse == "open" ? "closed" : "open";
  }
}
