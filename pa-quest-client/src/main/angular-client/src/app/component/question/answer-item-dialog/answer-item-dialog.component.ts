import { Component, OnInit, Inject } from '@angular/core';
import { DialogType } from 'src/app/app-constarts';
import { Question } from 'src/app/model/question';
import { QuestionService } from 'src/app/service/question.service';
import { MatDialogRef, MAT_DIALOG_DATA, PageEvent, MatDialog } from '@angular/material';
import { Answer } from 'src/app/model/answer';
import { map } from 'rxjs/operators';
import { LogService } from 'src/app/service/log.service';

@Component({
  selector: 'app-answer-item-dialog',
  templateUrl: './answer-item-dialog.component.html',
  styleUrls: ['./answer-item-dialog.component.css']
})
export class AnswerItemDialogComponent implements OnInit {

  titleDialog: string;
  typeDialog: DialogType;
  answerForm: Answer;


  constructor(
    private questionService: QuestionService,
    private dialogRef: MatDialogRef<AnswerItemDialogComponent>,
    @Inject(MAT_DIALOG_DATA) private data: any,
    private logService: LogService) { }

  ngOnInit() {
    this.titleDialog = this.data.title;
    this.typeDialog = this.data.type;
    this.answerForm = new Answer();
  }

  onSave(): void {
    this.questionService.addAnswer(this.answerForm).subscribe(result => {
      this.dialogRef.close(result);
    });
  }

  onCancel(): void {
    this.dialogRef.close();
  }

}
