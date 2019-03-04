import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { HomeComponent } from './component/home/home.component';
import { LoginComponent } from './component/login/login.component';
import { RegisterComponent } from './component/register/register.component';
import { AuthGuard } from './guard/auth-guard';
import { RoleGuard } from './guard/role-guard';
import { UserComponent } from './component/user/user.component';
import { StartQuestComponent } from './component/quest/start-quest/start-quest.component';
import { CongratulationComponent } from './component/congratulation/congratulation.component';
import { FinishQuestGuard } from './guard/finish-quest-guard';
import { QuestionListComponent } from './component/question/question-list/question-list.component';

const routes: Routes = [

  {
    path: 'home',
    component: HomeComponent,
    canActivate: [ AuthGuard ]
  },
  {
    path: 'start-quest',
    component: StartQuestComponent,
    canActivate: [ AuthGuard ]
  },
  {
    path: 'questions',
    component: QuestionListComponent,
    canActivate: [ RoleGuard ],
    data: { 
      expectedRole: 'admin'
    }
  },
  {
    path: 'congratulation',
    component: CongratulationComponent,
    canActivate: [ FinishQuestGuard ]
  },
  {
    path: 'user',
    component: UserComponent,
    canActivate: [ RoleGuard ],
    data: { 
      expectedRole: 'admin'
    } 
  },
  {
    path: 'auth/login',
    component: LoginComponent
  },
  {
    path: 'signup',
    component: RegisterComponent,
    canActivate: [ RoleGuard ], 
    data: { 
      expectedRole: 'admin'
    } 
  },
  {
    path: '',
    redirectTo: 'home',
    pathMatch: 'full'
  },
  {
    path: '**',
    redirectTo: 'home'
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
