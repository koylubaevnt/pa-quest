import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { LogService } from '../service/log.service';
import { TokenStorageService } from '../service/token-storage.service';
import { AuthenticationService } from '../service/authentication.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  
    constructor(private authService: AuthenticationService, 
        private router: Router, 
        private logService: LogService) {}

    canActivate(next: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        let url: string =  state.url;
        return this.checkLogin(url);
    }

    checkLogin(url: string): boolean {
        if (!this.authService.isAuthenticated()) {
            this.authService.redirectUrl = url;
            this.router.navigate(['/auth/login']);
            return false;
        }
        return true;        
    }
}