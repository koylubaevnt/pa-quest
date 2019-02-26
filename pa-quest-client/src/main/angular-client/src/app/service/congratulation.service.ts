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
export class CongratulationService {

    private url: string = environment.url;
    
    private congratulationUrl = this.url + '/congratulation';
    
    constructor(private http: HttpClient, private logService: LogService) {}

    /* Получить видеопоздравление */
    getVideoId(): Observable<any> {
        let url = this.congratulationUrl;
        this.logService.debug(`getVideoId(): Make request: ${url}`);
        return this.http.get<any>(`${url}`)
            .pipe(
                tap(_ => this.logService.debug(`getVideoId(): url=${url}`)),
                catchError(this.handleError('getVideoId()', null))
            )
    }

    public handleError<T>(operation='operation', result?: T) {
        return (error: any): Observable<T> => {
            this.logService.error(`handleError(): operation=${operation}, error.message=${error.message}`, error);

            return of(result as T);
        }
    }
}