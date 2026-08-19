import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReceptionistReservations } from './receptionist-reservations';

describe('ReceptionistReservations', () => {
  let component: ReceptionistReservations;
  let fixture: ComponentFixture<ReceptionistReservations>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ReceptionistReservations],
    }).compileComponents();

    fixture = TestBed.createComponent(ReceptionistReservations);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
