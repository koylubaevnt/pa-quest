import { Component, OnInit, Inject } from '@angular/core';
import { DialogType } from 'src/app/app-constarts';
import { QuestionForm } from 'src/app/model/question-form';
import { QuestionService } from 'src/app/service/question.service';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material';

@Component({
  selector: 'app-question-item-dialog',
  templateUrl: './question-item-dialog.component.html',
  styleUrls: ['./question-item-dialog.component.css']
})
export class QuestionItemDialogComponent implements OnInit {

  titleDialog: string;
  typeDialog: DialogType;

  questionForm: QuestionForm;

  editable: boolean = true;
  buttonSave: string = "Сохранить";

  constructor(private questionService: QuestionService,
    private dialogRef: MatDialogRef<QuestionItemDialogComponent>,
    @Inject(MAT_DIALOG_DATA) private data: any) { }

  ngOnInit() {
    this.titleDialog = this.data.title;
    this.typeDialog = this.data.type;
    if (this.typeDialog == DialogType.DELETE) {
      this.questionForm = this.data.question;
      this.editable = false;
      this.buttonSave = "Удалить";
    }
  }

  onSave(): void {
    if (this.typeDialog === DialogType.DELETE) {
      this.questionService.deleteQuestion(this.questionForm.id).subscribe(response => {
        if (response) {
          this.dialogRef.close(response);
        }
      });
    }
  }

  onCancel(): void {
    this.dialogRef.close();
  }
  
}
