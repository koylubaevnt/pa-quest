import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatProgressBarModule, MatTableModule, MatPaginatorModule, MatIconModule, MatButtonModule, MatProgressSpinnerModule, MatPaginatorIntl } from '@angular/material';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { LoginComponent } from './component/login/login.component';

import { httpInterceptorProviders } from './interceptor/auth-interceptor';
import { HomeComponent } from './component/home/home.component';
import { RegisterComponent } from './component/register/register.component';
import { LoaderComponent } from './component/loader/loader.component';
import { NavBarComponent } from './component/nav-bar/nav-bar.component';
import { UserComponent } from './component/user/user.component';
import { RussianPaginator } from './common/paginator/russian-paginator';
import { FooterComponent } from './component/footer/footer.component';

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
    MatProgressSpinnerModule
  ],
  providers: [
    httpInterceptorProviders,
    {
      provide: MatPaginatorIntl,
      useClass: RussianPaginator
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
