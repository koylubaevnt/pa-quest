import { Component, OnInit, Inject } from '@angular/core';
import { User } from 'src/app/model/user';
import { UserService } from 'src/app/service/user.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';
import { DialogType } from 'src/app/app-constarts';

@Component({
  selector: 'app-user-item-dialog',
  templateUrl: './user-item-dialog.component.html',
  styleUrls: ['./user-item-dialog.component.css']
})
export class UserItemDialogComponent implements OnInit {

  titleDialog: string;
  typeDialog: DialogType;
  buttonSaveText: string = "Сохранить";
  editable: boolean = true;
  user: User;

  admin: boolean = false;

  constructor(private userService: UserService, 
      private dialogRef: MatDialogRef<UserItemDialogComponent>,
      @Inject(MAT_DIALOG_DATA)private data: any) { }

  ngOnInit() {
    this.titleDialog = this.data.title;
    this.typeDialog = this.data.type;
    this.user = new User();
    this.admin = false;

    if (this.typeDialog === DialogType.UPDATE) {
      this.copyUser();
    } else if (this.typeDialog === DialogType.DELETE) {
      this.editable = false;
      this.copyUser();
      this.buttonSaveText = "Удалить";
    }
    
  }

  onSave(): void {

    if (this.admin && !this.user.role.includes('admin')) {
      this.user.role.push('admin');
    }

    if (this.typeDialog === DialogType.CREATE) {
      this.userService.addUser(this.user).subscribe(result => {
        this.dialogRef.close(result);
      });
    } else if (this.typeDialog === DialogType.UPDATE) {
      this.userService.updateUser(this.user).subscribe(result => {
        this.dialogRef.close(result);
      });
    } else if (this.typeDialog === DialogType.DELETE) {
      this.userService.deleteUser(this.user.id).subscribe(result => {
        this.dialogRef.close(result);
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  needPasswordInput(): boolean {
    return this.typeDialog != DialogType.DELETE;
  }

  private copyUser(): void {
    this.user.id = this.data.user.id;
    this.user.username = this.data.user.username;
    this.user.email = this.data.user.email;
    //this.user.password = this.data.user.password;
    //this.user.passwordConfirm = this.data.user.passwordConfirm;
    this.user.name = this.data.user.name;
    if (!this.data.user.role || this.data.user.role.length === 0) {
      this.user.role = [ "user" ]
    } else {
      this.user.role = [];
      for (let r of this.data.user.role) {
        this.user.role.push(r);
      }
      if (!this.user.role.includes('user')) {
        this.user.role.push('user');
      }
      if (!this.user.role.includes('admin')) {
        this.admin = true;
      }
    }
  }

}
