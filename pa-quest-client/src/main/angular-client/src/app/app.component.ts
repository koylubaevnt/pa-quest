import { Component, OnInit } from '@angular/core';
import { MatIconRegistry } from '@angular/material';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent implements OnInit {
  
  // constructor(iconRegistry: MatIconRegistry, sanitizer: DomSanitizer) {
  //   iconRegistry.addSvgIcon(
  //     'delete',
  //     sanitizer.bypassSecurityTrustResourceUrl('assets/img/icons/delete.svg'));
  //   iconRegistry.addSvgIcon(
  //     'edit',
  //     sanitizer.bypassSecurityTrustResourceUrl('assets/img/icons/edit.svg'));
  // }

  ngOnInit() {
    
  }
}
