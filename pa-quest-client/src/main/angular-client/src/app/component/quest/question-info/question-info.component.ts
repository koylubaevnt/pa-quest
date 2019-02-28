import { Component, OnInit, Input, EventEmitter, Output, OnChanges } from '@angular/core';
import { QuestionForm } from 'src/app/model/question-form';
import { SafeUrl, DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';
import { MatDialog } from '@angular/material';
import { Answer } from 'src/app/model/answer';
import { QuestionService } from 'src/app/service/question.service';
import { DialogType } from 'src/app/app-constarts';
import { LogService } from 'src/app/service/log.service';
import { AnswerItemDialogComponent } from '../../question/answer-item-dialog/answer-item-dialog.component';
import { AnswerSelectDialogComponent } from '../../question/answer-select-dialog/answer-select-dialog.component';
import { QuestionItemDialogComponent } from '../../question/question-item-dialog/question-item-dialog.component';

@Component({
  selector: 'app-question-info',
  templateUrl: './question-info.component.html',
  styleUrls: ['./question-info.component.css']
})
export class QuestionInfoComponent implements OnInit, OnChanges {

  @Input()
  question: QuestionForm;
  
  @Output()
  refreshQuestions: EventEmitter<any> = new EventEmitter();

  questionForm: QuestionForm;
  changed: boolean = false;

  constructor(private sanitizer: DomSanitizer, private dialog: MatDialog, private questionService: QuestionService, private logService: LogService) { }

  ngOnInit() {
  }

  ngOnChanges() {
    this.changeVal(false);
    this.questionForm = new QuestionForm();
    if (this.question && this.question.id) {
      this.questionForm = this.copyQuestion(this.question);
    } else {
      this.changeVal(true);
    }
  }

  deleteDialog(question: QuestionForm): void {
    if (question.id) {
      let dialogRef = this.dialog.open(QuestionItemDialogComponent, {
        width: '700px',
        height: '550px',
        data: {
          title: 'Подтверждение удаления вопроса',
          type: DialogType.DELETE,
          question: question
        }
      });
      dialogRef.afterClosed().subscribe(result => {
        if (result) {
          this.refreshQuestions.emit(null);
        }
      })
    } else {
      this.refreshQuestions.emit(null);
    }
  }

  changeYoutubeLink(value: any) {
    this.logService.debug('changeYoutubeLink', [value, this.questionForm.youtubeVideoUrl, this.question]);
    if (value && value != this.questionForm.youtubeVideoUrl) {
      let videoId: string = this.youTubeGetID(value)
      if (videoId) {
        this.questionForm.youtubeVideoUrl = 'http://youtube.com/embed/' + videoId;
        this.questionForm.youtubeVideoId = videoId;
        this.changeVal(true);
      } else {
        this.questionForm.youtubeVideoUrl = '';
        this.questionForm.youtubeVideoId = '';
      }
    }
  }

  changeVal(changed: boolean): void {
    this.changed = changed;
  }

  save(): void {
    if (!this.questionForm.id) {
      this.questionService.addQuestion(this.questionForm).subscribe(result => {
        if (result) {
          this.refreshQuestions.emit(result);
        } else {
          this.refreshQuestions.emit(null);
        } 
      }, error => {});
    } else {
      this.questionService.updateQuestion(this.questionForm).subscribe(result => {
        if (result) {
          this.refreshQuestions.emit(result);
        } else {
          this.questionForm = this.copyQuestion(this.question);
        } 
      }, error => {});
    }
  }

  cancel(): void {
    this.changeVal(false);
    if (this.question && this.question.id) {
      this.questionForm = this.copyQuestion(this.question);
    } else {
      this.refreshQuestions.emit(null);
    }
  }

  selectAnswer(index: number): void {
    this.logService.debug(`selectAnswer. val=${index}`)
    this.logService.debug(`selectCorrectAnswer`)
    let dialogRef = this.dialog.open(AnswerSelectDialogComponent, {
      width: '700px',
      height: '550px',
      data: {
        title: `Выбор ответа №${index + 1}`
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.questionForm.answers[index] = result;
        this.changeVal(true);
      }
    });
  }

  selectCorrectAnswer(): void {
    this.logService.debug(`selectCorrectAnswer`)
    let dialogRef = this.dialog.open(AnswerSelectDialogComponent, {
      width: '700px',
      height: '550px',
      data: {
        title: 'Выбор правильного ответа'
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.questionForm.correctAnswer = result;
        this.changeVal(true);
      }
    });
  }

  private youTubeGetID(url: any): string {
    let id: string = '';
    url = url.replace(/(>|<)/gi,'').split(/(vi\/|v=|\/v\/|youtu\.be\/|\/embed\/)/);
    if(url[2] !== undefined) {
      id = url[2].split(/[^0-9a-z_\-]/i);
      id = id[0];
    }
    else {
      id = url;
    }
      return id;
  }

  private copyQuestion(question: QuestionForm): QuestionForm {
    let questionForm: QuestionForm = this.questionForm;
    questionForm.id = question.id;
    questionForm.text = question.text;
    questionForm.youtubeVideoId = question.youtubeVideoId;
    if (question.youtubeVideoUrl) {
      questionForm.youtubeVideoUrl = question.youtubeVideoUrl;
    } else if (questionForm.youtubeVideoId) {
      questionForm.youtubeVideoUrl = 'http://youtube.com/embed/' + questionForm.youtubeVideoId;
    }

    let correctAnswer: Answer = new Answer();
    correctAnswer.id = question.correctAnswer.id;
    correctAnswer.text = question.correctAnswer.text;
    questionForm.correctAnswer = correctAnswer;
    
    let answers: Answer[] = [];
    for (let answer of question.answers) {
      let ans: Answer = new Answer();
      ans.id = answer.id;
      ans.text = answer.text;
      answers.push(ans);
    }
    questionForm.answers = answers;
    return questionForm;
  }
}
