import { Injectable } from '@angular/core';
import { LogPublisher, LogPublisherConfig } from '../log/log-publisher';
import { LogConsole } from '../log/log-console';
import { LogLocalStorage } from '../log/log-local-storage';
import { LogWebApi } from '../log/log-web-api';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { catchError, map } from 'rxjs/operators';

const PUBLISHERS_FILE = "assets/log-publishers.json";
    
@Injectable({
    providedIn: 'root'
})
export class LogPublishersService {
  
    publishers: LogPublisher[] = [];
        
    constructor(private http: HttpClient) {
        this.buildPublishers();
    }
      
    buildPublishers(): void {
        
        let logPublisher: LogPublisher;

        this.getLoggers()
            .subscribe(response => {
                for (let pub of response.filter(p => p.isActive)) {
                    switch (pub.loggerName.toLowerCase()) {
                        case "console":
                            logPublisher = new LogConsole();
                            break;
                        case "localstorage":
                            logPublisher = new LogLocalStorage();
                            break;
                        case "webapi":
                            logPublisher = new LogWebApi(this.http);
                            break;
                    }
                    logPublisher.location = pub.loggerLocation;
                    this.publishers.push(logPublisher);
                }
            })

        // this.publishers.push(new LogConsole());
        // this.publishers.push(new LogLocalStorage());
        // this.publishers.push(new LogWebApi(this.http));
    }

    getLoggers(): Observable<LogPublisherConfig[]> {
        return this.http.get<any>(PUBLISHERS_FILE)
            .pipe(
                map(response => response),
                catchError(this.handleErrors)
            );
    }

    private handleErrors(error: any): Observable<any> {
        let errors: string[] = [];
        let msg: string = "";
            
        msg = "Status: " + error.status;
        msg += " - Status Text: " + error.statusText;
        if (error.exceptionMessage) {
            msg += " - Exception Message: "
                + error.exceptionMessage;
        }
        errors.push(msg);
            
        console.error('An error occurred', errors);
            
        return Observable.throw(errors);
    }
}