import { Component, OnInit } from '@angular/core';
import { TokenStorageService } from 'src/app/service/token-storage.service';
import { UserQuestService } from 'src/app/service/user-quest.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit {

  info: any;

  constructor(private token: TokenStorageService, private userQuesrService: UserQuestService) { }

  ngOnInit() {
    this.info = {
      token: this.token.getToken(),
      username: this.token.getUsername(),
      name: this.token.getName(),
      roles: this.token.getRoles(),
      authorities: this.token.getAuthorities(),
      expiredDate: this.token.getExpiredDate(),
      issueDate: this.token.getIssueDate()
    };
    this.userQuesrService.isFinishQuest().subscribe(_ => _);
  }

  logout() {
    this.token.signOut();
    window.location.reload();
  }

}
