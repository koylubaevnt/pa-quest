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

    if (!this.user.roles) {
      this.user.roles = ['ROLE_USER']  
    }

    if (this.admin && !this.user.roles.includes('ROLE_ADMIN')) {
      this.user.roles.push('ROLE_ADMIN');
    } else if (!this.admin && this.user.roles.includes('ROLE_ADMIN')) {
      this.user.roles.splice(this.user.roles.indexOf('ROLE_ADMIN'), 1);
    }

    // for(let i = 0; i < this.user.roles.length; i++) {
    //   let role = 'ROLE_' + this.user.roles[i].toUpperCase().replace('ROLE_', '');
    //   this.user.roles[i] = role;
    // }

    if (this.typeDialog === DialogType.CREATE) {
      this.userService.addUser(this.user).subscribe(result => {
        if (result) {
          this.dialogRef.close(result);
        }
      });
    } else if (this.typeDialog === DialogType.UPDATE) {
      this.userService.updateUser(this.user).subscribe(result => {
        if (result) {
          this.dialogRef.close(result);
        }
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
    if (!this.data.user.roles || this.data.user.roles.length === 0) {
      this.user.roles = [ "ROLE_USER" ]
    } else {
      this.user.roles = [];
      for (let r of this.data.user.roles) {
        this.user.roles.push(r);
      }
      if (!this.user.roles.includes('ROLE_USER')) {
        this.user.roles.push('ROLE_USER');
      }
      if (this.user.roles.includes('ROLE_ADMIN')) {
        this.admin = true;
      }
    }
  }

}
