import { Question } from './question';

export class UserQuestion {
    
    public id: number;
    public start: string;
    public finish: string;
    public numberOfAttempts: number;
    public answered: boolean;
    public question: Question;

}