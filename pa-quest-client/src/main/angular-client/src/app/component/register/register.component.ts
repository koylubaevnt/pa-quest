import { Component, OnInit } from '@angular/core';
import { SignUpInfo } from 'src/app/model/sigup-info';
import { AuthenticationService } from 'src/app/service/authentication.service';
import { LogService } from 'src/app/service/log.service';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  form: any = {};
  signupInfo: SignUpInfo;
  isSignedUp = false;
  isSignUpFailed = false;
  errorMessage = '';

  constructor(private authService: AuthenticationService, private logService: LogService) { }

  ngOnInit() {
  }

  onSubmit() {
    this.logService.log('submit form', this.form);

    this.signupInfo = new SignUpInfo(
      this.form.name,
      this.form.username,
      this.form.email,
      this.form.password,
      this.form.passwordConfirm
    );

    this.authService.signUp(this.signupInfo)
      .subscribe(data => {
        this.logService.log(`User is signuped`, data);  
        this.isSignedUp = true;
        this.isSignUpFailed = false;
      },
      error => {
        this.logService.error(`Error while signup user`, error);
        this.errorMessage = error.error.message;
        this.isSignUpFailed = true;
      })
  }
}
