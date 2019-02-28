import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PageEvent } from '@angular/material';
import { DomSanitizer } from '@angular/platform-browser';
import { QuestionForm } from 'src/app/model/question-form';
import { LogService } from 'src/app/service/log.service';
import { QuestionService } from 'src/app/service/question.service';
import { Question } from 'src/app/model/question';

@Component({
  selector: 'app-question-list',
  templateUrl: './question-list.component.html',
  styleUrls: ['./question-list.component.css']
})
export class QuestionListComponent implements OnInit {

  questions: QuestionForm[];
  search: string = "";
  searchValue: string = "";

  selectedQuestion: QuestionForm;

  page: number = 0;
  pageIndex: number = 0;
  pageSize: number = 8;
  pageEvent: PageEvent;
  totalElements: number;

  constructor(private router: Router, private sanitizer: DomSanitizer, private logService: LogService, private questionService: QuestionService) { }

  ngOnInit() {
    this.getQuestionsPaging(null);
  }

  onSelectQuestion(question: QuestionForm): void {
    this.selectedQuestion = question;
    this.logService.debug("onSelectQuestion", this.selectedQuestion);
  }

  searchQuestions(): void {
    if (this.searchValue != null && this.searchValue.trim()) {
      this.search = `text~${this.searchValue}`;
    } else {
      this.search = "";
    }
    this.page = 0
    this.getQuestionsPaging(null);
  }

  clearSearch(): void {
    this.searchValue = null;
    this.searchQuestions();
  }

  setPage(event: PageEvent) {
    this.pageEvent = event;
    this.pageIndex = event.pageIndex;
    this.getQuestionsPaging(null);
  }

  getYoutubeUrl(videoId: string) {
    return this.sanitizer.bypassSecurityTrustResourceUrl(`https://youtube.com/embed/${videoId}`);
  }

  createQuestion(): void {
    this.selectedQuestion = new QuestionForm();
  }

  editQuestion(questionId: number) {

    console.log('edit: ' + questionId);
  }

  deleteQuestion(questionId: number) {
    console.log('delete: ' + questionId);
  }

  getQuestionsPaging(activeQuestion: QuestionForm) {
    this.questionService.getQuestionsPadding(this.page, this.pageSize, this.search)
      .subscribe(result => {
        this.questions = result['data'];
        this.totalElements = result['totalElements'];
        this.pageIndex = result['number'];
      },
      
      error => {},
      () => {
        if (this.questions) {
          if (!activeQuestion) {
            this.onSelectQuestion(this.questions[0]);
          } else {
            this.onSelectQuestion(activeQuestion);
          }
        }
      })
  }

}
