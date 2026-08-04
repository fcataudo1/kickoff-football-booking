import { Component, AfterViewInit, ViewChild, ElementRef } from '@angular/core';

@Component({
  selector: 'app-video-section',
  imports: [],
  templateUrl: './video-section.html',
  styleUrl: './video-section.css',
})
export class VideoSectionComponent implements AfterViewInit {


  @ViewChild('videoPlayer') video!: ElementRef<HTMLVideoElement>;


  ngAfterViewInit(){

    const video = this.video.nativeElement;

    video.muted = true;
    video.volume = 0;

    video.play();

  }


}