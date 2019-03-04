import { Injectable } from "@angular/core";
import { environment } from 'src/environments/environment';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { tap, catchError, map } from 'rxjs/operators';
import { LogService } from './log.service';
import { User } from '../model/user';
import { DataResponse } from '../model/data-response';
import { Congratulation } from '../model/congratulation';

@Injectable({
    providedIn: 'root'
})
export class CongratulationService {

    private url: string = environment.url;
    
    private congratulationUrl = this.url + '/congratulation';
    
    constructor(private http: HttpClient, private logService: LogService) {}

    /* Получить видеопоздравление */
    getCongratulation(): Observable<any> {
        let url = this.congratulationUrl;
        this.logService.debug(`getCongratulation(): Make request: ${url}`);
        return this.http.get<any>(`${url}`)
            .pipe(
                tap(_ => this.logService.debug(`getCongratulation(): url=${url}`)),
                catchError(this.handleError('getCongratulation()', null))
            )
    }

   /** Добавление поздравления */
   addCongratulation(configuration: Congratulation): Observable<Congratulation> {
    return this.http.post<any>(`${this.congratulationUrl}`, configuration)
        .pipe(
            map((resp: DataResponse) => resp.data),
            tap(_ => this.logService.debug(`addCongratulation(): configuration.id=${configuration.id}`)),
            catchError(this.handleError('addCongratulation()', null))
        );
    }

    /**
     * Изменение поздравления
     * 
     * @param configuration  Поздравление 
     */
    updateCongratulation(configuration: Congratulation): Observable<Congratulation> {
        return this.http.put<any>(`${this.congratulationUrl}`, configuration)
            .pipe(
                map((resp: DataResponse) => resp.data),
                tap(_ => this.logService.debug(`updateCongratulation(): configuration.id=${configuration.id}`)),
                catchError(this.handleError('updateCongratulation()', null))
            );
    }

    /**
     * Удаление поздравления
     * 
     * @param configurationId    Идентификатор поздравления 
     */
    deleteCongratulation(configurationId: number): Observable<Congratulation> {
        return this.http.delete<Congratulation>(`${this.congratulationUrl}/${configurationId}`)
        .pipe(
            tap(_ => this.logService.debug(`deleteCongratulation(): id=${configurationId}`)),
            catchError(this.handleError('deleteCongratulation()', null))
        );
    }
    public handleError<T>(operation='operation', result?: T) {
        return (error: any): Observable<T> => {
            this.logService.error(`handleError(): operation=${operation}, error.message=${error.message}`, error);

            return of(result as T);
        }
    }
}