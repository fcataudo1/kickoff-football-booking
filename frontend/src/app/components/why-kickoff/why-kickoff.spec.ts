import { ComponentFixture, TestBed } from '@angular/core/testing';

import { WhyKickoff } from './why-kickoff';

describe('WhyKickoff', () => {
  let component: WhyKickoff;
  let fixture: ComponentFixture<WhyKickoff>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [WhyKickoff],
    }).compileComponents();

    fixture = TestBed.createComponent(WhyKickoff);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
