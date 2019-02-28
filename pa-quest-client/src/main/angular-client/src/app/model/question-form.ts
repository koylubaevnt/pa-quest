import { Content } from './content';
import { Answer } from './answer';

export class QuestionForm {
    
    public id: number;
    public text: string;
    public youtubeVideoId: string;
    //public content: Content;
    public correctAnswer: Answer;
    public answers: Answer[];

    public youtubeVideoUrl?: string;
}