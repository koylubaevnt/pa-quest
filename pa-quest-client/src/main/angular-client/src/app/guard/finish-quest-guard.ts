import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, Router } from '@angular/router';
import { LogService } from '../service/log.service';
import { UserQuestService } from '../service/user-quest.service';
import { map, catchError } from 'rxjs/operators';
import { Observable, Subject, BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FinishQuestGuard implements CanActivate {
  
    constructor(private userQuestService: UserQuestService, 
        private router: Router, 
        private logService: LogService) {}


    private result: BehaviorSubject<Boolean> = new BehaviorSubject<Boolean>(false);

    canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
        let url: string =  state.url;
        
        return this.userQuestService.isFinishQuest().pipe(
            map(e => e.data),
            catchError(e => null)
        );
    }

    
}