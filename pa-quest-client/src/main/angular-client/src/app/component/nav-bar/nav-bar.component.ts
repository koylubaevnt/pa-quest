import { Component, OnInit } from '@angular/core';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { TokenStorageService } from 'src/app/service/token-storage.service';

@Component({
  selector: 'app-nav-bar',
  templateUrl: './nav-bar.component.html',
  styleUrls: ['./nav-bar.component.css']
})
export class NavBarComponent implements OnInit {
  
  private roles: string[] = [];
  private authorized: boolean = false;

  constructor(private tokenStorageService: TokenStorageService, private authService: AuthenticationService) { }

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
    })
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
}
