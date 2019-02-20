import { LogPublisher } from './log-publisher';
import { LogEntry } from '../service/log.service';
import { Observable, of } from 'rxjs';

export class LogLocalStorage extends LogPublisher {
    
    constructor() {
        super();
        this.location = "logging";
    }
        
    log(entry: LogEntry): Observable<boolean> {
        let ret: boolean = false;
        let values: LogEntry[];
            
        try {
            values = JSON.parse(
                localStorage.getItem(this.location))
                    || [];
            values.push(entry);
            localStorage.setItem(this.location,
                                JSON.stringify(values));
            ret = true;
        } catch (ex) {
            console.log(ex);
        }
            
        return of(ret);
    }
        
    clear(): Observable<boolean> {
        localStorage.removeItem(this.location);
        return of(true);
    }
    
}