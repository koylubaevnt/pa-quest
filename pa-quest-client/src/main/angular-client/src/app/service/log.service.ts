import { Injectable } from '@angular/core';
import { LogPublishersService } from './log-publisher.service';
import { LogPublisher } from '../log/log-publisher';

export enum LogLevel {
    ALL = 0,
    DEBUG = 1,
    INFO = 2,
    WARN = 3,
    ERROR = 4,
    FATAL = 5,
    OFF = 6
  }

@Injectable({
    providedIn: 'root'
})
export class LogService {

    private level: LogLevel = LogLevel.ALL;
    private logWithDate: boolean = true;
    private publishers: LogPublisher[];

    constructor(private publishersService: LogPublishersService) {
        this.publishers = this.publishersService.publishers;
    }

    log(msg: string, ...params: any[]) {
        this.writeToLog(msg, LogLevel.ALL, params);
    }

    debug(msg: string, ...params: any[]) {
        this.writeToLog(msg, LogLevel.DEBUG, params);
    }

    info(msg: string, params: any[]) {
        this.writeToLog(msg, LogLevel.INFO, params);
    }

    warn(msg: string, params: any[]) {
        this.writeToLog(msg, LogLevel.WARN, params);
    }

    error(msg: string, params: any[]) {
        this.writeToLog(msg, LogLevel.ERROR, params);
    }

    fatal(msg: string, params: any[]) {
        this.writeToLog(msg, LogLevel.FATAL, params);
    }

    private writeToLog(msg: string, level: LogLevel, params: any[]) {
        if (this.shouldLog(level)) {
            let entry: LogEntry = new LogEntry();
            entry.message = msg;
            entry.level = level;
            entry.extraInfo = params;
            entry.logWithDate = this.logWithDate;
            
            //console.log(entry.buildLogString());
            for (let logger of this.publishers) {
                logger.log(entry)
                    .subscribe(response => 
                        console.log(response));
            }
        }
    }

    private shouldLog(level: LogLevel): boolean {
        let ret: boolean = false;
        if ((level >= this.level &&
                level !== LogLevel.OFF) ||
                this.level === LogLevel.ALL) {
            ret = true;
        }
        return ret;
    }

    private formatParams(params: any[]): string {
        let ret: string = params.join(",");
            
        if (params.some(p => typeof p == "object")) {
            ret = "";
            for (let item of params) {
                ret += JSON.stringify(item) + ",";
            }
        }
            
        return ret;
    }  
}

export class LogEntry {
    // Public Properties
    entryDate: Date = new Date();
    message: string = "";
    level: LogLevel = LogLevel.DEBUG;
    extraInfo: any[] = [];
    logWithDate: boolean = true;
        
    buildLogString(): string {
      let ret: string = "";
        
      if (this.logWithDate) {
        ret = new Date() + " - ";
      }
      ret += "Type: " + LogLevel[this.level];
      ret += " - Message: " + this.message;
      if (this.extraInfo.length) {
        ret += " - Extra Info: "
          + this.formatParams(this.extraInfo);
      }
        
      return ret;
    }
        
    private formatParams(params: any[]): string {
      let ret: string = params.join(",");
      if (params.some(p => typeof p == "object")) {
        ret = "";
        for (let item of params) {
          ret += JSON.stringify(item) + ",";
        }
      }
      return ret;
    }
  }