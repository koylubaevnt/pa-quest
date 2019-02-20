import { DataSource } from '@angular/cdk/table';
import { User } from 'src/app/model/user';
import { BehaviorSubject, Observable, of } from 'rxjs';
import { UserService } from 'src/app/service/user.service';
import { CollectionViewer } from '@angular/cdk/collections';
import { catchError, finalize, map } from 'rxjs/operators';


export class UserDataSource implements DataSource<User> {

    private userSubject = new BehaviorSubject<User[]>([]);
    private loadingSubject = new BehaviorSubject<boolean>(false);
    
    private lengthSubject = new BehaviorSubject<number>(0);

    public loading$ = this.loadingSubject.asObservable();

    constructor(private userService: UserService) {}

    connect(collectionViewer: CollectionViewer): Observable<User[]> {
        return this.userSubject.asObservable();
    }

    disconnect(collectionViewer: CollectionViewer): void {
        this.userSubject.complete();
        this.loadingSubject.complete();
    }

    length(): Observable<number> {
        return this.lengthSubject.asObservable();
    }

    loadUsers(pageIndex = 0, pageSize = 3, filter = '') {

        this.loadingSubject.next(true);

        this.userService.getUsersPadding(pageIndex, pageSize, filter).pipe(
            map(data => {
                this.lengthSubject.next(data.totalElements);
                return data.data;
            }),
            catchError(() => of([])),
            finalize(() => this.loadingSubject.next(false)),
        )
        .subscribe(users => this.userSubject.next(users));
    }    
}
