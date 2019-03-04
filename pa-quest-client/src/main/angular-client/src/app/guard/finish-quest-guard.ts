import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { LogService } from '../service/log.service';
import { UserQuestService } from '../service/user-quest.service';
import { map, catchError } from 'rxjs/operators';
import { Observable, Subject, BehaviorSubject, of } from 'rxjs';
import { AuthenticationService } from '../service/authentication.service';

@Injectable({
  providedIn: 'root'
})
export class FinishQuestGuard implements CanActivate {
  
    constructor(private userQuestService: UserQuestService, 
        private authService: AuthenticationService,
        private router: Router, 
        private logService: LogService) {}


    private result: BehaviorSubject<Boolean> = new BehaviorSubject<Boolean>(false);

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
        let url: string =  state.url;
        if (this.authService.isAuthenticated()) {
            return this.userQuestService.isFinishQuest().pipe(
                map(e => e.data),
                catchError(e => null)
            );
        } else {
            this.authService.redirectUrl = url;
            this.router.navigate(['/auth/login']);
            return of(false);
        }
    }

    
}