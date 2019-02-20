import { LogPublisher } from './log-publisher';
import { LogEntry } from '../service/log.service';
import { Observable, of } from 'rxjs';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { map, catchError } from 'rxjs/operators';
import { environment } from 'src/environments/environment';

export class LogWebApi extends LogPublisher {
    
    private url: string = environment.url;

    constructor(private http: HttpClient) {
      super();
      this.location = "log";
    }
        
    // Add log entry to back end data store
    log(entry: LogEntry): Observable<boolean> {
      let headers = new HttpHeaders(
       { 'Content-Type': 'application/json' });

      let options = { 
          headers: headers
        };
        
      return this.http.post<any>(`${this.url}/${this.location}`, entry, options)
        .pipe(
            map(response => response),
            catchError(this.handleErrors)
        );
    }

    clear(): Observable<boolean> {
      // TODO: Call Web API to clear all values
      return of(true);
    }
        
    private handleErrors(error: any):
                   Observable<any> {
      let errors: string[] = [];
      let msg: string = "";
        
      msg = "Status: " + error.status;
      msg += " - Status Text: " + error.statusText;
      if (error.json()) {
        msg += " - Exception Message: " + error.json().exceptionMessage;
      }
      errors.push(msg);
      console.error('An error occurred', errors);
      return Observable.throw(errors);
    }
  }