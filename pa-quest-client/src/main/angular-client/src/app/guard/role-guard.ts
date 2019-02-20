import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { LogService } from '../service/log.service';
import { TokenStorageService } from '../service/token-storage.service';
import { AuthenticationService } from '../service/authentication.service';

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {
  
    constructor(private tokenStorageService: TokenStorageService, 
        private authService: AuthenticationService, 
        private router: Router, 
        private logService: LogService) {}

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): boolean {
        let url: string =  state.url;
        const expectedRole = route.data.expectedRole;
        
        let hasExpectedRole: boolean = this.tokenStorageService.getRoles().some(role => role === expectedRole);
        
        this.logService.debug(`url=${url}, expectedRole=${expectedRole}, hasExpectedRole=${hasExpectedRole}`);
        
        if (!this.authService.isAuthenticated()) {
            this.authService.redirectUrl = url;
            this.router.navigate(['/auth/login']);
            return false;
        }
        if (!hasExpectedRole) {
            this.router.navigate(['/home']);
            return false;
        }
        return true;
    }

    
}