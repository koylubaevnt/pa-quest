import { Component, OnInit } from '@angular/core';
import { MatSnackBar } from '@angular/material';

@Component({
  selector: 'app-info-bar',
  templateUrl: './info-bar.component.html',
  styleUrls: ['./info-bar.component.css']
})
export class InfoBarComponent implements OnInit {


  constructor(private snackBar: MatSnackBar) { }

  ngOnInit() {
  }

  openSnackBar(message: string, action: string, className: string): any {
    this.snackBar.open(
      message,
      action,
      {
        duration: 10000,
        verticalPosition: 'top',
        horizontalPosition: 'center',
        panelClass: [className]
      }
    );
  }
}
