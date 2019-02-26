import { Injectable } from "@angular/core";
import { environment } from 'src/environments/environment';
import { Observable, of, BehaviorSubject } from 'rxjs';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { tap, catchError, map } from 'rxjs/operators';
import { LogService } from './log.service';
import { User } from '../model/user';
import { DataResponse } from '../model/data-response';

@Injectable({
    providedIn: 'root'
})
export class UserQuestService {

    oneQuestIsFinished$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

    private url: string = environment.url;
    
    private questUrl = this.url + '/quest';
    
    constructor(private http: HttpClient, private logService: LogService) {}

    /* Есть ли хотя бы один неоконченный КВЕСТ? */
    isFinishQuest(): Observable<any> {
        let url = this.questUrl + '/finished';
        this.logService.debug(`isFinishQuest(): Make request: ${url}`);
        return this.http.get<any>(`${url}`)
            .pipe(
                tap(data => {
                    this.logService.debug(`isFinishQuest(): url=${url}`, data)
                    this.oneQuestIsFinished$.next(data.data);
                }),
                catchError(this.handleError('isFinishQuest()', null))
            )
    }

    /* Начать КВЕСТ */
    getUserQuest(): Observable<any> {
        let url = this.questUrl + '/run';
        this.logService.debug(`getUserQuest(): Make request: ${url}`);
        return this.http.get<any>(`${url}`)
            .pipe(
                tap(_ => this.logService.debug(`getUserQuest(): url=${url}`)),
                catchError(this.handleError('getUserQuest()', null))
            )
    }

    /* Сохранение пользовательского ответа */
    saveAnswer(userQuestId: number, userQuestionId: number, userAnswerId: number): Observable<any> {
        let url = this.questUrl + '/run';

        let obj: any = { 
            userQuestId: userQuestId,
            userQuestionId : userQuestionId,
            userAnswerId: userAnswerId
        };

        this.logService.debug(`saveAnswer(): Make request: ${url}`, obj);

        return this.http.put<any>(`${url}`, obj)
        .pipe(
            tap(_ => this.logService.debug(`saveAnswer(): obj=${obj}`)),
            catchError(this.handleError('saveAnswer()', null))
        );
    }

    // /* Получение пользователя по id */
    // getUser(user: User): Observable<User> {
    //      return this.http.get<User>(`${this.url}/${user.id}`)
    //     .pipe(
    //         tap(_ => this.logService.debug(`getUser(): username=${user.username}`)),
    //         catchError(this.handleError('getUser()', null))
    //     );
    // }

    // /* Добавление пользователя */
    // addUser(user: User): Observable<User> {
    //     return this.http.post<User>(`${this.url}`, user)
    //     .pipe(
    //         tap(_ => this.logService.debug(`addUser(): username=${user.username}`)),
    //         catchError(this.handleError('addUser()', null))
    //     );
    // }
    
    // /* Удаление пользователя */
    // deleteUser(userId: number): Observable<User> {
    //     return this.http.delete<User>(`${this.url}/${userId}`)
    //     .pipe(
    //         tap(_ => this.logService.debug(`deleteUser(): id=${userId}`)),
    //         catchError(this.handleError('deleteUser()', null))
    //     );
    // }

    public handleError<T>(operation='operation', result?: T) {
        return (error: any): Observable<T> => {
            this.logService.error(`handleError(): operation=${operation}, error.message=${error.message}`, error);

            return of(result as T);
        }
    }
}