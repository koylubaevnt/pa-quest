import { Injectable } from "@angular/core";
import { environment } from 'src/environments/environment';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { tap, catchError, map } from 'rxjs/operators';
import { LogService } from './log.service';
import { User } from '../model/user';
import { DataResponse } from '../model/data-response';

@Injectable({
    providedIn: 'root'
})
export class UserService {

    private url: string = environment.url;
    
    private userUrl = this.url + '/user';
    
    constructor(private http: HttpClient, private logService: LogService) {}

    /* Получить список пользователей */
    // getUsersPadding(page: number, pageSize: number, searchString: string): Observable<any> {
    //     this.logService.debug(`getUsersPadding(): Make request: ${this.userUrl}?page=${page}&page-size=${pageSize}&search=${searchString}`);
    //     const request = new HttpRequest('GET', `${this.userUrl}?page=${page}&page-size=${pageSize}&search=${searchString}`, {
    //         reportRequest: true
    //     })
    //     return this.http.request<any>(request)
    //         .pipe(
    //             map((event: HttpEvent<DataResponse>) => event),
    //             tap(_ => this.logService.debug(`getUsersPadding(): url=${this.userUrl}, page=${page}, page-size=${pageSize}, search=${searchString}`)),
    //             catchError(this.handleError('getUsersPadding()', []))
    //         )
    // }

    getUsersPadding(page: number, pageSize: number, searchString: string): Observable<any> {
        this.logService.debug(`getUsersPadding(): Make request: ${this.userUrl}?page=${page}&page-size=${pageSize}&search=${searchString}`);
        return this.http.get<any>(`${this.userUrl}?page=${page}&page-size=${pageSize}&search=${searchString}`)
            .pipe(
                tap(_ => this.logService.debug(`getUsersPadding(): url=${this.userUrl}, page=${page}, page-size=${pageSize}, search=${searchString}`)),
                catchError(this.handleError('getUsersPadding()', []))
            )
    }

    /* Получение пользователя по id */
    getUser(user: User): Observable<User> {
         return this.http.get<User>(`${this.url}/${user.id}`)
        .pipe(
            tap(_ => this.logService.debug(`getUser(): username=${user.username}`)),
            catchError(this.handleError('getUser()', null))
        );
    }

    /* Добавление пользователя */
    addUser(user: User): Observable<User> {
        return this.http.post<User>(`${this.url}`, user)
        .pipe(
            tap(_ => this.logService.debug(`addUser(): username=${user.username}`)),
            catchError(this.handleError('addUser()', null))
        );
    }
    
    /* Удаление пользователя */
    deleteUser(userId: number): Observable<User> {
        return this.http.delete<User>(`${this.url}/${userId}`)
        .pipe(
            tap(_ => this.logService.debug(`deleteUser(): id=${userId}`)),
            catchError(this.handleError('deleteUser()', null))
        );
    }

    public handleError<T>(operation='operation', result?: T) {
        return (error: any): Observable<T> => {
            this.logService.error(`handleError(): operation=${operation}, error.message=${error.message}`, error);

            return of(result as T);
        }
    }
}