import { Content } from './content';
import { Answer } from './answer';

export class Question {
    
    public id: number;
    public text: string;
    public youtubeVideoId: string;
    //public content: Content;
    public answers: Answer[];

}