import {
  ComponentFixture,
  TestBed
} from '@angular/core/testing';

import { VideoSectionComponent } from './video-section';

describe('VideoSectionComponent', () => {

  let component: VideoSectionComponent;
  let fixture: ComponentFixture<VideoSectionComponent>;

  beforeEach(async () => {

    await TestBed.configureTestingModule({
      imports: [
        VideoSectionComponent
      ]
    }).compileComponents();

    fixture =
      TestBed.createComponent(
        VideoSectionComponent
      );

    component =
      fixture.componentInstance;

    await fixture.whenStable();

  });


  it('should create', () => {

    expect(component)
      .toBeTruthy();

  });

});