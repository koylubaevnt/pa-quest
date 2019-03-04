import { Component, OnInit, SecurityContext } from '@angular/core';
import { UserQuestService } from 'src/app/service/user-quest.service';
import { LogService } from 'src/app/service/log.service';
import { LoaderService } from 'src/app/service/loader.service';
import { map, catchError } from 'rxjs/operators';
import { of } from 'rxjs';
import { UserQuest } from 'src/app/model/user-quest';
import { UserQuestion } from 'src/app/model/user-question';
import { Answer } from 'src/app/model/answer';
import { SafeResourceUrl, DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-start-quest',
  templateUrl: './start-quest.component.html',
  styleUrls: ['./start-quest.component.css']
})
export class StartQuestComponent implements OnInit {

  questFinished: boolean = false;
  userQuest: UserQuest;
  currentQuestion: UserQuestion;
  currentIndex: number = 1;
  countQuestion: number;
 // previousQuestionId: number;
  
  question: String;
  videoUrl: SafeResourceUrl;


  constructor(private userQuestService: UserQuestService, private logService: LogService, private loaderService: LoaderService,
      private sanitizer: DomSanitizer) { }

  ngOnInit() {
  }

  ngAfterViewInit() {
    this.loadUserQuest();
    this.userQuestService.isFinishQuest().subscribe(_ => _); 
  }

  loadUserQuest() {
    this.userQuestService.getUserQuest().pipe(
      map(data => {
          return data.data;
      }),
      catchError(() => of(null))
    )
    .subscribe((userQuest: UserQuest) => this.nextQuest(userQuest));
  }

  startQuest() {
    this.userQuestService.getUserQuest().pipe(
      map(data => {
          return data.data;
      }),
      catchError(() => of(null))
    )
    .subscribe((userQuest: UserQuest) => {
      this.questFinished = false;
      this.nextQuest(userQuest)
    });
  }

  saveAnswer(answer: Answer) {
    // TODO: отправить ответ на вопрос
    this.logService.debug('Отправляем ответ пользователя', answer);
    this.userQuestService.saveAnswer(this.userQuest.id, this.currentQuestion.id, answer.id).pipe(
        map(data => {
            return data.data;
        }),
        catchError(() => of(null))
      )
      .subscribe((userQuest: UserQuest) => this.nextQuest(userQuest));
  }

  private nextQuest(userQuest: UserQuest) {
    if (userQuest == null) {
      this.logService.error(`Квест не получен`, null);
      return;
    }
    if (this.userQuest && userQuest.id == this.userQuest.id) {
      // Одинаковые квесты:
      // Поменяться могло:
      // 1. активность квеста
      // 2. количество попыток
      // 3. вопрос
      if (this.userQuest.active != userQuest.active) {
        // Активность поменялась!
        this.userQuest = userQuest;
        if (!this.userQuest.active) {
          this.logService.debug(`Квест был закончен...`);
          this.questFinished = true;
          this.currentQuestion = undefined;
          this.question = undefined;
          this.videoUrl = undefined;
          this.currentIndex = undefined;
          this.countQuestion = undefined;
          this.userQuestService.isFinishQuest().subscribe(_ => _);
        }    
      } else {
        let idx: number = 0;
        for (let userQuestion of userQuest.questions) {
          if (!userQuestion.answered) {
            if (this.currentQuestion.id === userQuestion.id) {
              this.currentQuestion.numberOfAttempts = userQuestion.numberOfAttempts;
            } else {
              this.currentIndex = idx + 1;
              this.currentQuestion = userQuestion;
              this.question = userQuestion.question.text;
              this.videoUrl = this.sanitizer.bypassSecurityTrustResourceUrl('https://youtube.com/embed/' + userQuestion.question.youtubeVideoId);  
            }
            break;
          }
          idx++;
        }
        
      }

    } else {
      // новый квест
      this.userQuest = userQuest;
      
      this.countQuestion = userQuest.questions.length;
      this.logService.debug(`Получили квест ${userQuest.active} - ${this.userQuest.active}`, this.userQuest);
      if (!this.userQuest.active) {
        this.logService.debug(`Квест был закончен...`);
        this.questFinished = true;
        this.currentQuestion = undefined;
        this.question = undefined;
        this.videoUrl = undefined;
        this.currentIndex = undefined;
        this.countQuestion = undefined;
        this.userQuestService.isFinishQuest().subscribe(_ => _);
      } else {
        let idx: number = 0;
        for (let userQuestion of userQuest.questions) {
          if (!userQuestion.answered) {
            this.currentIndex = idx + 1;
            this.currentQuestion = userQuestion;
            this.question = this.currentQuestion.question.text;
            this.videoUrl = this.sanitizer.bypassSecurityTrustResourceUrl('https://youtube.com/embed/' + this.currentQuestion.question.youtubeVideoId);
            break;
          }
          idx++;
        }
        //this.previousQuestionId = this.currentQuestion.id;
      }
    }
    
  }
}
