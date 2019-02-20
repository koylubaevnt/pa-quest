import { Component, OnInit } from '@angular/core';
import { AuthLoginInfo } from 'src/app/model/login-info';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { TokenStorageService } from 'src/app/service/token-storage.service';
import { LogService } from 'src/app/service/log.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  form: any = {};
  isLoggedIn = false;
  isLoginFailed = false;
  errorMessage = '';
  roles: string[] = [];
  private loginInfo: AuthLoginInfo;

  constructor(
      private authService: AuthenticationService, 
      private tokenStorageService: TokenStorageService, 
      private router: Router,
      private logService: LogService) { }

  ngOnInit() {
    if (this.tokenStorageService.getToken()) {
      this.isLoggedIn = true;
      this.roles = this.tokenStorageService.getRoles();
    }
  }

  onSubmit() {
    this.logService.log('Submit form', this.form);

    this.loginInfo = new AuthLoginInfo(
      this.form.username,
      this.form.password
    );

    this.authService.attemptAuth(this.loginInfo)
      .subscribe(data => {
        this.tokenStorageService.saveToken(data.token);
        //this.tokenStorageService.saveUsername(data.username);
        //this.tokenStorageService.saveAuthorities(data.authorities);

        this.isLoginFailed = false;
        this.isLoggedIn = true;
        this.roles = this.tokenStorageService.getRoles();
        
        this.authService.loggedIn.next(true)

        if (!this.authService.redirectUrl) {
          this.router.navigate(['/home']);
        } else {
          this.router.navigateByUrl(this.authService.redirectUrl);
        }        
        //this.reloadPage();
      },
      error => {
        this.logService.error(`Error while trying autorization`, error);
        this.errorMessage = error.error.message;
        this.isLoginFailed = true;
        this.authService.loggedIn.next(false)
      })
  }

  reloadPage() {
    window.location.reload();
  }
}
