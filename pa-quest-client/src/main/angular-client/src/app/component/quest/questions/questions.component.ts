import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { PageEvent } from '@angular/material';
import { DomSanitizer } from '@angular/platform-browser';
import { Question } from 'src/app/model/question';
import { QuestionForm } from 'src/app/model/question-form';
import { LogService } from 'src/app/service/log.service';

@Component({
  selector: 'app-questions',
  templateUrl: './questions.component.html',
  styleUrls: ['./questions.component.css']
})
export class QuestionsComponent implements OnInit {

  questions: QuestionForm[];
  search: string = "";
  searchValue: string = "";

  selectedQuestion: QuestionForm;

  page: number = 0;
  pageIndex: number = 0;
  pageSize: number = 8;
  pageEvent: PageEvent;
  totalElements: number;

  constructor(private router: Router, private sanitizer: DomSanitizer, private logService: LogService) { }

  ngOnInit() {
    this.getQuestionsPaging();
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
    this.getQuestionsPaging();
  }

  clearSearch(): void {
    this.searchValue = null;
    this.searchQuestions();
  }

  setPage(event: PageEvent) {
    this.pageEvent = event;
    this.pageIndex = event.pageIndex;
    this.getQuestionsPaging();
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
  
  getQuestionPaging(event: any) {
    this.getQuestionsPaging();
  }

  private getQuestionsPaging() {
    this.mokup();
  }

  private mokup() {
    this.questions = [];
    for(let i = 0; i < this.pageSize; i++) {
      this.questions.push(
        {
          id: i + 1,
          text: "Что это за памятник? " + (i + 1),
          youtubeVideoId: "Ok81Ue2mu0A",
          correctAnswer: {
              id: 1,
              text: "Памятник комунистам"
          },
          answers: [
            { 
              id: 1,
              text: "Памятник комунистам"
            },
            { 
              id: 2,
              text: "Очень интересный памятник"
            },
            { 
              id: 3,
              text: "Да хрен его знает"
            },
            { 
              id: 4,
              text: "Памятник финам"
            }
          ]
        }
      );
    }
  }
}
