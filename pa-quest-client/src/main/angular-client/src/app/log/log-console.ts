import { LogPublisher } from './log-publisher';
import { LogEntry } from '../service/log.service';
import { of, Observable } from 'rxjs';

export class LogConsole extends LogPublisher {
    log(entry: LogEntry): Observable<boolean> {
        // Log to console
        console.log(entry.buildLogString());
        return of(true);
    }
    clear(): Observable<boolean> {
        console.clear();
        return of(true);
    }
}