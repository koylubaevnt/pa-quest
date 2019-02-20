import { Injectable } from '@angular/core';
import decode from 'jwt-decode';

const TOKEN_KEY = 'AuthToken';
const TOKEN_USERNAME_KEY = 'AuthUsername';
const TOKEN_ISSUE_DATE_KEY = 'AuthIssueDate';
const TOKEN_EXPIRED_DATE_KEY = 'AuthExpiredDate';
const TOKEN_AUTHORITIES_KEY = 'AuthAuthorities';
const TOKEN_ROLES_KEY = 'AuthRoles';

@Injectable({
    providedIn: 'root'
})
export class TokenStorageService {
    
    private autorities: Array<string> = [];
    private roles: Array<string> = [];

    constructor() {}

    signOut() {
        sessionStorage.clear();
    }

    public saveToken(token: string) {
        sessionStorage.removeItem(TOKEN_KEY);
        sessionStorage.setItem(TOKEN_KEY, token);
        
        let tokenPayload = decode(token);
        
        sessionStorage.removeItem(TOKEN_USERNAME_KEY);
        sessionStorage.setItem(TOKEN_USERNAME_KEY, tokenPayload.sub);
        sessionStorage.removeItem(TOKEN_ISSUE_DATE_KEY);
        sessionStorage.setItem(TOKEN_ISSUE_DATE_KEY, tokenPayload.iat);
        sessionStorage.removeItem(TOKEN_EXPIRED_DATE_KEY);
        sessionStorage.setItem(TOKEN_EXPIRED_DATE_KEY, tokenPayload.exp);
        sessionStorage.removeItem(TOKEN_ROLES_KEY);
        sessionStorage.setItem(TOKEN_ROLES_KEY, JSON.stringify(tokenPayload.roles));
        sessionStorage.removeItem(TOKEN_AUTHORITIES_KEY);
        sessionStorage.setItem(TOKEN_AUTHORITIES_KEY, JSON.stringify(tokenPayload.authorities));

    }

    public getToken(): string {
        return sessionStorage.getItem(TOKEN_KEY);
    }

    public getUsername(): string {
        return sessionStorage.getItem(TOKEN_USERNAME_KEY);
    }

    public getAuthorities(): string[] {
         this.autorities = [];

         if (sessionStorage.getItem(TOKEN_KEY)) {
             JSON.parse(sessionStorage.getItem(TOKEN_AUTHORITIES_KEY)).forEach(authority => {
                 this.autorities.push(authority.toUpperCase());
             });
         }
        return this.autorities;
    }

    public getRoles(): string[] {
        this.roles = [];

        if (sessionStorage.getItem(TOKEN_KEY)) {
            JSON.parse(sessionStorage.getItem(TOKEN_ROLES_KEY)).forEach(role => {
                this.roles.push(role.toLowerCase());
            });
        }

        return this.roles;
    }

    public getExpiredDate(): Date {
        var date = new Date(0);
        date.setUTCSeconds(Number.parseInt(sessionStorage.getItem(TOKEN_EXPIRED_DATE_KEY)));
        return date;
    }

    public getIssueDate(): Date {
        var date = new Date(0);
        date.setUTCSeconds(Number.parseInt(sessionStorage.getItem(TOKEN_ISSUE_DATE_KEY)));
        return date;
    }
}