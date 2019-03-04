import { Component, OnInit, OnChanges } from '@angular/core';
import { CongratulationService } from 'src/app/service/congratulation.service';
import { DomSanitizer } from '@angular/platform-browser';
import { map, catchError } from 'rxjs/operators';
import { Congratulation } from 'src/app/model/congratulation';

@Component({
  selector: 'app-congratulation-edit',
  templateUrl: './congratulation-edit.component.html',
  styleUrls: ['./congratulation-edit.component.css']
})
export class CongratulationEditComponent implements OnInit {

  congratulation: Congratulation;
  currentCongratulation: Congratulation = new Congratulation();
  changed: boolean = false;

  constructor(private congratulationService: CongratulationService, private sanitizer: DomSanitizer) { }

  ngOnInit() {
  }

  ngAfterViewInit() {
    this.congratulationService.getCongratulation().pipe(
      map(data => data.data),
      catchError(err => null)
    )
    .subscribe(
      result => {
        this.congratulation = result;
        this.currentCongratulation = this.copyCongratuation(this.congratulation);
      }
    );
  }


  changeYoutubeLink(value: any) {
    if (value && value != this.currentCongratulation.youtubeVideoUrl) {
      let videoId: string = this.youTubeGetID(value)
      if (videoId) {
        this.currentCongratulation.youtubeVideoUrl = 'http://youtube.com/embed/' + videoId;
        this.currentCongratulation.videoId = videoId;
        this.changeVal(true);
      } else {
        this.currentCongratulation.youtubeVideoUrl = '';
        this.currentCongratulation.videoId = '';
      }
    }
  }

  changeVal(changed: boolean): void {
    this.changed = changed;
  }

  save(): void {
    if (!this.currentCongratulation.id) {
      this.congratulationService.addCongratulation(this.currentCongratulation).subscribe(result => {
        if (result) {
          this.congratulation = result;
          this.currentCongratulation = this.copyCongratuation(this.congratulation);
          this.changeVal(false);
        } 
      }, error => {});
    } else {
      this.congratulationService.updateCongratulation(this.currentCongratulation).subscribe(result => {
        if (result) {
          this.congratulation = result;
          this.currentCongratulation = this.copyCongratuation(this.congratulation);
          this.changeVal(false);
        } 
      }, error => {});
    }
  }

  cancel(): void {
    this.changeVal(false);
    if (this.congratulation && this.congratulation.id) {
      this.currentCongratulation = this.copyCongratuation(this.congratulation);
    } else {
      this.currentCongratulation = new Congratulation();
    }
  }

  private copyCongratuation(congratuation: Congratulation): Congratulation {
    if (!congratuation) {
      return new Congratulation();
    }
    let tmp: Congratulation = this.currentCongratulation;
    tmp.id = congratuation.id;
    if (congratuation.videoId) {
      tmp.youtubeVideoUrl = 'http://youtube.com/embed/' + congratuation.videoId;
      tmp.videoId = congratuation.videoId;
    } 
    return tmp;
  }

  private youTubeGetID(url: any): string {
    let id: string = '';
    url = url.replace(/(>|<)/gi,'').split(/(vi\/|v=|\/v\/|youtu\.be\/|\/embed\/)/);
    if(url[2] !== undefined) {
      id = url[2].split(/[^0-9a-z_\-]/i);
      id = id[0];
    }
    else {
      id = url[0];
    }
      return id;
  }
}
