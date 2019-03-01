import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { FlexLayoutModule } from '@angular/flex-layout';

import { MatProgressBarModule, MatTableModule, MatPaginatorModule, MatIconModule, MatButtonModule, MatProgressSpinnerModule, MatPaginatorIntl, MatCardModule, MatGridListModule, MatFormFieldModule, MatInputModule, MatListModule, MatDialogModule, MatCheckboxModule, MatSnackBarModule } from '@angular/material';

import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './component/login/login.component';

import { httpInterceptorProviders, AuthInterceptor } from './interceptor/auth-interceptor';
import { HomeComponent } from './component/home/home.component';
import { RegisterComponent } from './component/register/register.component';
import { LoaderComponent } from './component/loader/loader.component';
import { NavBarComponent } from './component/nav-bar/nav-bar.component';
import { UserComponent } from './component/user/user.component';
import { RussianPaginator } from './common/paginator/russian-paginator';
import { FooterComponent } from './component/footer/footer.component';
import { StartQuestComponent } from './component/quest/start-quest/start-quest.component';
import { CongratulationComponent } from './component/congratulation/congratulation.component';
import { QuestionListComponent } from './component/question/question-list/question-list.component';
import { QuestionInfoComponent } from './component/question/question-info/question-info.component';
import { SafePipe } from './common/pipes/safe.pipe';
import { AnswerItemDialogComponent } from './component/question/answer-item-dialog/answer-item-dialog.component';
import { AnswerSelectDialogComponent } from './component/question/answer-select-dialog/answer-select-dialog.component';
import { QuestionItemDialogComponent } from './component/question/question-item-dialog/question-item-dialog.component';
import { UserItemDialogComponent } from './component/user/user-item-dialog/user-item-dialog.component';
import { InfoBarComponent } from './component/info-bar/info-bar.component';

@NgModule({
  declarations: [
    AppComponent,
    LoginComponent,
    HomeComponent,
    RegisterComponent,
    UserComponent,
    LoaderComponent,
    NavBarComponent,
    FooterComponent,
    StartQuestComponent,
    CongratulationComponent,
    QuestionListComponent,
    QuestionInfoComponent,

    SafePipe,

    AnswerItemDialogComponent,

    AnswerSelectDialogComponent,

    QuestionItemDialogComponent,

    UserItemDialogComponent,

    InfoBarComponent

  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    HttpClientModule,

    BrowserAnimationsModule,
    MatProgressBarModule,
    MatTableModule,
    MatPaginatorModule,
    MatIconModule,
    MatButtonModule,
    MatProgressSpinnerModule,
    MatCardModule,
    MatGridListModule,
    MatFormFieldModule,
    MatInputModule,
    MatListModule,
    MatDialogModule,
    MatCheckboxModule,
    MatSnackBarModule,

    FlexLayoutModule,
    
    NgbModule.forRoot()
  ],
  entryComponents: [
    AnswerItemDialogComponent,
    AnswerSelectDialogComponent,
    QuestionItemDialogComponent,

    UserItemDialogComponent
  ],
  providers: [
    httpInterceptorProviders,
    {
      provide: MatPaginatorIntl,
      useClass: RussianPaginator
    },
    InfoBarComponent
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
