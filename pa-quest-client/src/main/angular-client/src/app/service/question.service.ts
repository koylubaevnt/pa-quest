import { Injectable } from "@angular/core";
import { environment } from 'src/environments/environment';
import { Observable, of, BehaviorSubject } from 'rxjs';
import { HttpClient, HttpRequest, HttpEvent } from '@angular/common/http';
import { tap, catchError, map } from 'rxjs/operators';
import { LogService } from './log.service';
import { User } from '../model/user';
import { DataResponse } from '../model/data-response';
import { QuestionForm } from '../model/question-form';
import { Answer } from '../model/answer';

const ANSWERS_DATA = [];
for(let i = 0; i < 20; i++) {
    ANSWERS_DATA.push({
        id: i+1,
        text: "Ответ №" + (i+1)
    })
}

const QUESTIONS_DATA = [];
for(let i = 0; i < 20; i++) {
    QUESTIONS_DATA.push(
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

@Injectable({
    providedIn: 'root'
})
export class QuestionService {

    oneQuestIsFinished$: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(false);

    private url: string = environment.url;
    
    private questionUrl = this.url + '/question';
    private answerUrl = this.url + '/answer';
    
    constructor(private http: HttpClient, private logService: LogService) {}

    /**
     * Список вопросов
     * 
     * @param page              Номер страницы 
     * @param pageSize          Размер страницы
     * @param searchString      Строка поиска
     */
    getQuestionsPadding(page: number, pageSize: number, searchString: string): Observable<any> {
        this.logService.debug(`getAnswersPadding(): Make request: ${this.questionUrl}?page=${page}&page-size=${pageSize}&search=${searchString}`);
        return of({
            data: QUESTIONS_DATA,
            totalElements: 50,
            size: 20,
            number: 0,
            numberOfElements: 20}).pipe(
                tap(resp=> this.logService.debug(`getQuestionsPadding(): url=${this.questionUrl}, page=${page}, page-size=${pageSize}, search=${searchString}`, resp)),
                catchError(this.handleError('getQuestionsPadding()', []))
                );
        return this.http.get<any>(`${this.questionUrl}?page=${page}&page-size=${pageSize}&search=${searchString}`)
            .pipe(
                tap(_ => this.logService.debug(`getAnswersPadding(): url=${this.questionUrl}, page=${page}, page-size=${pageSize}, search=${searchString}`)),
                catchError(this.handleError('getAnswersPadding()', []))
            )
    }

    /** Добавление вопроса */
    addQuestion(questionForm: QuestionForm): Observable<QuestionForm> {
        return this.http.post<QuestionForm>(`${this.questionUrl}`, questionForm)
            .pipe(
                tap(_ => this.logService.debug(`addQuestion(): questionForm.id=${questionForm.id}`)),
                catchError(this.handleError('addQuestion()', null))
            );
    }

    /**
     * Изменение вопроса
     * 
     * @param questionForm  Вопрос 
     */
    updateQuestion(questionForm: QuestionForm): Observable<QuestionForm> {
        return this.http.put<QuestionForm>(`${this.questionUrl}`, questionForm)
            .pipe(
                tap(_ => this.logService.debug(`updateQuestion(): questionForm.id=${questionForm.id}`)),
                catchError(this.handleError('updateQuestion()', null))
            );
    }

    /**
     * Удаление вопроса
     * 
     * @param questionId    Идентификатор вопроса 
     */
    deleteQuestion(questionId: number): Observable<QuestionForm> {
        return this.http.delete<QuestionForm>(`${this.questionUrl}`)
        .pipe(
            tap(_ => this.logService.debug(`deleteQuestion(): id=${questionId}`)),
            catchError(this.handleError('deleteQuestion()', null))
        );
    }

    /**
     *  Список ответов 
     * 
     * @param page          Номер страницы
     * @param pageSize      Размер страница
     * @param searchString  Строка поиска 
     */
    getAnswersPadding(page: number, pageSize: number, searchString: string): Observable<any> {
        this.logService.debug(`getAnswersPadding(): Make request: ${this.answerUrl}?page=${page}&page-size=${pageSize}&search=${searchString}`);
        // return this.http.get<any>(`${this.answerUrl}?page=${page}&page-size=${pageSize}&search=${searchString}`)
        //     .pipe(
        //         tap(_ => this.logService.debug(`getAnswersPadding(): url=${this.answerUrl}, page=${page}, page-size=${pageSize}, search=${searchString}`)),
        //         catchError(this.handleError('getAnswersPadding()', []))
        //     );
        return of({
            data: ANSWERS_DATA,
            totalElements: 50,
            size: 20,
            number: 0,
            numberOfElements: 20
        }).pipe(
            tap(resp=> this.logService.debug(`getAnswersPadding(): url=${this.answerUrl}, page=${page}, page-size=${pageSize}, search=${searchString}`, resp)),
            catchError(this.handleError('getAnswersPadding()', []))
            );
    }

    /**
     * Добавление ответа
     * 
     * @param answer    Ответ
     */
    addAnswer(answer: Answer): Observable<Answer> {
        ANSWERS_DATA.push(answer);
        answer.id = ANSWERS_DATA.length;
        return of(answer);

        return this.http.post<User>(`${this.answerUrl}`, answer)
            .pipe(
                tap(_ => this.logService.debug(`addAnswer(): username=${answer.id}`)),
                catchError(this.handleError('addAnswer()', null))
            );
    }



    // /* Получение пользователя по id */
    // getUser(user: User): Observable<User> {
    //      return this.http.get<User>(`${this.url}/${user.id}`)
    //     .pipe(
    //         tap(_ => this.logService.debug(`getUser(): username=${user.username}`)),
    //         catchError(this.handleError('getUser()', null))
    //     );
    // }

    // /* Добавление пользователя */
    // addUser(user: User): Observable<User> {
    //     return this.http.post<User>(`${this.url}`, user)
    //     .pipe(
    //         tap(_ => this.logService.debug(`addUser(): username=${user.username}`)),
    //         catchError(this.handleError('addUser()', null))
    //     );
    // }
    
    // /* Удаление пользователя */
    // deleteUser(userId: number): Observable<User> {
    //     return this.http.delete<User>(`${this.url}/${userId}`)
    //     .pipe(
    //         tap(_ => this.logService.debug(`deleteUser(): id=${userId}`)),
    //         catchError(this.handleError('deleteUser()', null))
    //     );
    // }

    public handleError<T>(operation='operation', result?: T) {
        return (error: any): Observable<T> => {
            this.logService.error(`handleError(): operation=${operation}, error.message=${error.message}`, error);

            return of(result as T);
        }
    }
}