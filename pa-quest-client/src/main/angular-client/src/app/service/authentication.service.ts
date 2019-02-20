import { Injectable, Output, EventEmitter } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { JwtHelperService } from '@auth0/angular-jwt';
import { Observable, of, BehaviorSubject } from 'rxjs';
import { environment } from 'src/environments/environment';

import { LogService } from './log.service';
import { AuthLoginInfo } from '../model/login-info';
import { JwtResponse } from '../model/jwt-response';
import { SignUpInfo } from '../model/sigup-info';
import { TokenStorageService } from './token-storage.service';

import decode from 'jwt-decode';
import { tap } from 'rxjs/operators';

const httpOptions = {
    headers: new HttpHeaders({ 'Content-Type': 'application/json' })
  };

@Injectable({
    providedIn: 'root'
})
export class AuthenticationService {

    private url: string = environment.url;
    private jwtHelper: JwtHelperService;
    private loginUrl = this.url + '/auth/signin';
    private signupUrl = this.url + '/auth/signup';
    
    loggedIn = new BehaviorSubject<boolean>(false); // {1}

    redirectUrl: string;

    constructor(private http: HttpClient, private tokenStorageService: TokenStorageService, private logService: LogService) {
        this.jwtHelper = new JwtHelperService();
    }
    
    isLoggedIn() {
        return this.loggedIn.asObservable(); // {2}
    }

    isAuthenticated(): boolean {
        const token: string = this.tokenStorageService.getToken();
        if (token) {
            const tokenPayload = decode(token);
            this.logService.log(`tokenPayload`, tokenPayload)
            // TODO истек токен перезапросить новый!
            return !this.jwtHelper.isTokenExpired(token);
        }
        return false;
    }

    attemptAuth(credentials: AuthLoginInfo): Observable<JwtResponse> {
        return this.http.post<JwtResponse>(this.loginUrl, credentials, httpOptions);
            // .pipe(
            //     tap(data => this.loggedIn.next(true),
            //     error => this.loggedIn.next(false))
            // );
      }
     
    signUp(info: SignUpInfo): Observable<string> {
        return this.http.post<string>(this.signupUrl, info, httpOptions);
    }

}