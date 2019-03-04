import { Component, OnInit, ViewChild } from '@angular/core';
import { MatTableDataSource, MatPaginator, PageEvent, MatIconRegistry, MatPaginatorIntl, MatDialog } from '@angular/material';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/service/user.service';
import { LogService } from 'src/app/service/log.service';
import { LoaderService } from 'src/app/service/loader.service';
import { HttpEventType } from '@angular/common/http';
import { DomSanitizer } from '@angular/platform-browser';
import { UserDataSource } from 'src/app/common/datasource/user-datasource';
import { tap } from 'rxjs/operators';
import { UserItemDialogComponent } from './user-item-dialog/user-item-dialog.component';
import { DialogType } from 'src/app/app-constarts';

@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent implements OnInit {

  // MatPaginator Inputs
  length = 100;
  pageSize = 10;
  pageSizeOptions: number[] = [5, 10, 20, 30, 40, 50];
  // MatPaginator Output
  pageEvent: PageEvent;

  displayedColumns: string[] = ['name', 'username', 'email', "actions"];
  dataSource: UserDataSource;

  @ViewChild(MatPaginator) paginator: MatPaginator;

  constructor(private userService: UserService, private logService: LogService, private loaderService: LoaderService,
    private dialog: MatDialog) { 
    
  }
  
  ngOnInit() {
    this.dataSource = new UserDataSource(this.userService);
    this.dataSource.length()
      .subscribe(length => this.length = length);
  }

  ngAfterViewInit() {
    this.paginator.page
        .pipe(
            tap(() => this.loadUsersPage())
        )
        .subscribe();
    this.loadUsersPage(); 
  }

  loadUsersPage() {
    this.dataSource.loadUsers(
        this.paginator.pageIndex,
        this.paginator.pageSize,
        '');
  }

  add() {
    let dialogRef = this.dialog.open(UserItemDialogComponent, {
      width: '500px',
      height: '500px',
      data: {
        title: "Создать пользователя",
        type: DialogType.CREATE
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadUsersPage();
      }
    });
  }
  
  edit(user: User) {
    let dialogRef = this.dialog.open(UserItemDialogComponent, {
      width: '500px',
      height: '500px',
      data: {
        title: "Изменить пользователя",
        type: DialogType.UPDATE,
        user: user
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadUsersPage();
      }
    });
  }
  
  delete(user: User) {
    let dialogRef = this.dialog.open(UserItemDialogComponent, {
      width: '500px',
      height: '400px',
      data: {
        title: "Подтверждение удаления пользователя",
        type: DialogType.DELETE,
        user: user
      }
    });

    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.loadUsersPage();
      }
    });  
  }

  mail(user?: User) {
    console.log(user);
    if (user) {
      this.userService.genPasswordForUser(user)
        .subscribe(_ => {});
    } else {
      this.userService.genPasswordForUsers()
        .subscribe(_ => {});
    }
  }

  // private makeRequest(page: number, pageSize: number, search: string) {
  //   this.userService.getUsersPadding(page, pageSize, search)
  //     .subscribe(event => {
  //       this.logService.debug(`event=${event.type}`, event);

  //       if(event.type === HttpEventType.DownloadProgress) {
  //         this.loaderService.show();
  //       } else if(event.type === HttpEventType.Response) {
  //         let data = event.body
  //         if (data.response_code === 200) {
  //           this.dataSource.data = data.data;
  //           this.length = data.totalElements;
  //         } else {
  //           this.logService.error(`Error while retrive data from server`, data);
  //         } 
  //         this.loaderService.hide();
  //       } else {
  //         this.loaderService.hide();
  //       }

        
  //     });
  // }
}
