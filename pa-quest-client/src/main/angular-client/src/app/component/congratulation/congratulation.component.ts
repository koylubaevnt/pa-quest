import { Component, OnInit } from '@angular/core';
import { CongratulationService } from 'src/app/service/congratulation.service';
import { map, catchError } from 'rxjs/operators';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-congratulation',
  templateUrl: './congratulation.component.html',
  styleUrls: ['./congratulation.component.css']
})
export class CongratulationComponent implements OnInit {

  videoId: string;

  constructor(private congratulationService: CongratulationService, private sanitizer: DomSanitizer) { }

  ngOnInit() {
  }

  ngAfterViewInit() {
    this.congratulationService.getVideoId().pipe(
      map(data => data.data),
      catchError(err => null)
    )
    .subscribe(
      data => {
        this.videoId = data;
      }
    );
  }

  getVideoUrl() {
    return this.sanitizer.bypassSecurityTrustResourceUrl('https://youtube.com/embed/' + this.videoId);
  }
}
