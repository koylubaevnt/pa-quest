import { Component, OnInit, Inject } from '@angular/core';
import { Answer } from 'src/app/model/answer';
import { PageEvent, MatDialogRef, MAT_DIALOG_DATA, MatDialog } from '@angular/material';
import { QuestionService } from 'src/app/service/question.service';
import { LogService } from 'src/app/service/log.service';
import { AnswerItemDialogComponent } from '../answer-item-dialog/answer-item-dialog.component';
import { DialogType } from 'src/app/app-constarts';

@Component({
  selector: 'app-answer-select-dialog',
  templateUrl: './answer-select-dialog.component.html',
  styleUrls: ['./answer-select-dialog.component.css']
})
export class AnswerSelectDialogComponent implements OnInit {


  titleDialog: string;
  
  answers: Answer[];
  selectedAnswer: Answer;

  page: number = 0;
  pageIndex: number = 0;
  pageSize: number = 20;
  totalElements: number;
  pageEvent: PageEvent;

  search: string = "";
  searchValue: string = "";

  constructor(
    private questionService: QuestionService,
    private dialogRef: MatDialogRef<AnswerSelectDialogComponent>,
    private dialog: MatDialog,
    @Inject(MAT_DIALOG_DATA) private data: any,
    private logService: LogService) { }

  ngOnInit() {
    this.titleDialog = this.data.title;
    this.getAnswerPadding();
  }

  setPage(event: PageEvent): void {
    this.pageEvent = event;
    this.page = event.pageIndex;
    this.getAnswerPadding();
  }
  
  onSelectAnswer(answer: Answer): void {
    this.selectedAnswer = answer;
    this.dialogRef.close(this.selectedAnswer);
  }

  onCancel(): void {
    this.dialogRef.close();
  }

  searchAnswers(): void {
    if (this.searchValue && this.searchValue.trim()) {
      this.search = `text~${this.searchValue}`;
    } else {
      this.search = "";
    }
    this.page = 0;
    this.getAnswerPadding();
  } 

  clearSearch(): void {
    this.searchValue = null;
    this.searchAnswers();
  }

  addAnswer(): void {
    let dialogRef = this.dialog.open(AnswerItemDialogComponent, {
      data: {
        title: 'Добавление нового ответа',
        type: DialogType.CREATE
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        // TODO: запись добавлена!
        this.getAnswerPadding();
      }
    })
  }

  private getAnswerPadding(): void {
    this.questionService.getAnswersPadding(this.page, this.pageSize, this.search)
      .subscribe(result => {
        this.logService.debug('get data', result)
        this.answers = result['data'];
        this.totalElements = result['totalElements'];
        this.pageIndex = result['number'];
        this.logService.debug('answers', this.answers)
      });
  }

}
