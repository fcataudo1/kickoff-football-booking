import {
  ComponentFixture,
  TestBed
} from '@angular/core/testing';

import { WhyKickoffComponent } from './why-kickoff';


describe('WhyKickoffComponent', () => {

  let component: WhyKickoffComponent;
  let fixture: ComponentFixture<WhyKickoffComponent>;


  beforeEach(async () => {

    await TestBed.configureTestingModule({

      imports: [
        WhyKickoffComponent
      ]

    }).compileComponents();


    fixture =
      TestBed.createComponent(
        WhyKickoffComponent
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