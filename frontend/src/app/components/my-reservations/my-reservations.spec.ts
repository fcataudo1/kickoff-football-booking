import {
  ComponentFixture,
  TestBed
} from '@angular/core/testing';

import {
  HttpTestingController,
  provideHttpClientTesting
} from '@angular/common/http/testing';

import {
  provideHttpClient
} from '@angular/common/http';

import {
  MyReservationsComponent
} from './my-reservations';

import {
  Reservation
} from '../../models/reservation';

import {
  vi
} from 'vitest';


describe('MyReservationsComponent', () => {

  let component: MyReservationsComponent;

  let fixture: ComponentFixture<MyReservationsComponent>;

  let httpTestingController: HttpTestingController;


  // =================================================
  // MOCK PRENOTAZIONE
  // =================================================

  const confirmedReservation: Reservation = {

    id: 1,

    reservationDate: '2026-08-25',

    startTime: '18:00',

    status: 'CONFIRMED',

    price: 50,

    footballField: {

      id: 1,

      name: 'Campo 1',

      active: true

    },

    user: {

      id: 1,

      nome: 'Mario',

      cognome: 'Rossi',

      email: 'mario@email.it',

      telefono: '3331234567',

      ruolo: 'CLIENTE'

    }

  };


  const confirmedReservation2: Reservation = {

    id: 2,

    reservationDate: '2026-08-27',

    startTime: '20:00',

    status: 'CONFIRMED',

    price: 50,

    footballField: {

      id: 2,

      name: 'Campo 2',

      active: true

    },

    user: {

      id: 1,

      nome: 'Mario',

      cognome: 'Rossi',

      email: 'mario@email.it',

      telefono: '3331234567',

      ruolo: 'CLIENTE'

    }

  };


  const cancelledReservation: Reservation = {

    id: 3,

    reservationDate: '2026-08-30',

    startTime: '19:00',

    status: 'CANCELLED',

    price: 50,

    footballField: {

      id: 1,

      name: 'Campo 1',

      active: true

    },

    user: {

      id: 1,

      nome: 'Mario',

      cognome: 'Rossi',

      email: 'mario@email.it',

      telefono: '3331234567',

      ruolo: 'CLIENTE'

    }

  };


  const pastReservation: Reservation = {

    id: 4,

    reservationDate: '2026-08-15',

    startTime: '17:00',

    status: 'CONFIRMED',

    price: 50,

    footballField: {

      id: 3,

      name: 'Campo 3',

      active: true

    },

    user: {

      id: 1,

      nome: 'Mario',

      cognome: 'Rossi',

      email: 'mario@email.it',

      telefono: '3331234567',

      ruolo: 'CLIENTE'

    }

  };


  // =================================================
  // SETUP
  // =================================================

  beforeEach(async () => {

    await TestBed.configureTestingModule({

      imports: [
        MyReservationsComponent
      ],

      providers: [

        provideHttpClient(),

        provideHttpClientTesting()

      ]

    }).compileComponents();


    fixture =
      TestBed.createComponent(
        MyReservationsComponent
      );


    component =
      fixture.componentInstance;


    httpTestingController =
      TestBed.inject(
        HttpTestingController
      );

  });


  afterEach(() => {

    httpTestingController.verify();

  });


  // =================================================
  // CREAZIONE
  // =================================================

  it('should create', () => {

    expect(component)
      .toBeTruthy();

  });


  // =================================================
  // INIT
  // =================================================

  it('should load reservations on init', () => {

    component.ngOnInit();


    expect(component.initialLoading)
      .toBe(true);


    const request =
      httpTestingController.expectOne(
        'http://localhost:8080/api/reservations/my'
      );


    expect(request.request.method)
      .toBe('GET');


    request.flush([
      confirmedReservation
    ]);


    expect(component.reservations)
      .toEqual([
        confirmedReservation
      ]);


    expect(component.initialLoading)
      .toBe(false);


    expect(component.errorMessage)
      .toBe('');

  });


  // =================================================
  // CARICAMENTO
  // =================================================

  it('should load reservations successfully', () => {

    component.loadReservations();


    expect(component.initialLoading)
      .toBe(true);


    const request =
      httpTestingController.expectOne(
        'http://localhost:8080/api/reservations/my'
      );


    expect(request.request.method)
      .toBe('GET');


    request.flush([

      confirmedReservation,

      confirmedReservation2

    ]);


    expect(component.reservations)
      .toEqual([

        confirmedReservation,

        confirmedReservation2

      ]);


    expect(component.initialLoading)
      .toBe(false);


    expect(component.errorMessage)
      .toBe('');

  });


  // =================================================
  // ERRORE CARICAMENTO
  // =================================================

  it('should handle error while loading reservations', () => {

    component.loadReservations();


    const request =
      httpTestingController.expectOne(
        'http://localhost:8080/api/reservations/my'
      );


    request.flush(

      'Server error',

      {
        status: 500,
        statusText: 'Internal Server Error'
      }

    );


    expect(component.initialLoading)
      .toBe(false);


    expect(component.errorMessage)
      .toBe(
        'Non è stato possibile caricare le tue prenotazioni.'
      );

  });


  // =================================================
  // FUTURE
  // =================================================

  it('should return upcoming reservations', () => {

    component.reservations = [

      confirmedReservation,

      confirmedReservation2,

      pastReservation

    ];


    const upcoming =
      component.upcomingReservations;


    expect(upcoming.length)
      .toBe(2);


    expect(upcoming)
      .toContain(confirmedReservation);


    expect(upcoming)
      .toContain(confirmedReservation2);


    expect(upcoming)
      .not.toContain(pastReservation);

  });


  // =================================================
  // ESCLUDE ANNULLATE
  // =================================================

  it('should exclude cancelled reservations from upcoming reservations', () => {

    component.reservations = [

      confirmedReservation,

      cancelledReservation

    ];


    const upcoming =
      component.upcomingReservations;


    expect(upcoming.length)
      .toBe(1);


    expect(upcoming[0])
      .toEqual(
        confirmedReservation
      );


    expect(upcoming)
      .not.toContain(
        cancelledReservation
      );

  });


  // =================================================
  // STORICO
  // =================================================

  it('should return past reservations', () => {

    component.reservations = [

      confirmedReservation,

      pastReservation

    ];


    const past =
      component.pastReservations;


    expect(past.length)
      .toBe(1);


    expect(past[0])
      .toEqual(
        pastReservation
      );

  });


  // =================================================
  // ANNULLATE NELLO STORICO
  // =================================================

  it('should include cancelled reservations in history', () => {

    component.reservations = [

      confirmedReservation,

      cancelledReservation

    ];


    const past =
      component.pastReservations;


    expect(past.length)
      .toBe(1);


    expect(past[0])
      .toEqual(
        cancelledReservation
      );

  });


  // =================================================
  // DATA
  // =================================================

  it('should format date correctly', () => {

    const result =
      component.formatDate(
        '2026-08-25'
      );


    expect(result)
      .toContain('25');


    expect(result)
      .toContain('agosto');


    expect(result)
      .toContain('2026');

  });


  // =================================================
  // ORA
  // =================================================

  it('should format time correctly', () => {

    expect(
      component.formatTime(
        '18:00:00'
      )
    )
      .toBe('18:00');


    expect(
      component.formatTime(
        '21:30:00'
      )
    )
      .toBe('21:30');

  });


  // =================================================
  // CONFERMATA
  // =================================================

  it('should identify confirmed reservation', () => {

    expect(
      component.isConfirmed(
        confirmedReservation
      )
    )
      .toBe(true);


    expect(
      component.isConfirmed(
        cancelledReservation
      )
    )
      .toBe(false);

  });


  // =================================================
  // ANNULLATA
  // =================================================

  it('should identify cancelled reservation', () => {

    expect(
      component.isCancelled(
        cancelledReservation
      )
    )
      .toBe(true);


    expect(
      component.isCancelled(
        confirmedReservation
      )
    )
      .toBe(false);

  });


  // =================================================
  // APRI MODALE
  // =================================================

  it('should open cancel modal for confirmed reservation', () => {

    component.openCancelModal(
      confirmedReservation
    );


    expect(component.showCancelModal)
      .toBe(true);


    expect(component.reservationToCancel)
      .toEqual(
        confirmedReservation
      );

  });


  // =================================================
  // NON APRIRE MODALE
  // =================================================

  it('should not open cancel modal for cancelled reservation', () => {

    component.openCancelModal(
      cancelledReservation
    );


    expect(component.showCancelModal)
      .toBe(false);


    expect(component.reservationToCancel)
      .toBeNull();

  });


  // =================================================
  // CHIUDI MODALE
  // =================================================

  it('should close cancel modal', () => {

    component.showCancelModal =
      true;


    component.reservationToCancel =
      confirmedReservation;


    component.cancelLoading =
      false;


    component.closeCancelModal();


    expect(component.showCancelModal)
      .toBe(false);


    expect(component.reservationToCancel)
      .toBeNull();

  });


  // =================================================
  // NON CHIUDERE DURANTE CANCELLAZIONE
  // =================================================

  it('should not close cancel modal while cancellation is loading', () => {

    component.showCancelModal =
      true;


    component.reservationToCancel =
      confirmedReservation;


    component.cancelLoading =
      true;


    component.closeCancelModal();


    expect(component.showCancelModal)
      .toBe(true);


    expect(component.reservationToCancel)
      .toEqual(
        confirmedReservation
      );

  });


  // =================================================
  // CANCELLA SENZA PRENOTAZIONE
  // =================================================

  it('should not cancel when no reservation is selected', () => {

    component.reservationToCancel =
      null;


    component.cancelReservation();


    expect(component.cancelLoading)
      .toBe(false);

  });


  // =================================================
  // CANCELLAZIONE RIUSCITA
  // =================================================

  it('should cancel reservation successfully', () => {

    component.reservationToCancel =
      confirmedReservation;


    component.showCancelModal =
      true;


    component.cancelReservation();


    expect(component.cancelLoading)
      .toBe(true);


    const deleteRequest =
      httpTestingController.expectOne(
        'http://localhost:8080/api/reservations/1'
      );


    expect(deleteRequest.request.method)
      .toBe('DELETE');


    deleteRequest.flush(null);


    expect(component.cancelLoading)
      .toBe(false);


    expect(component.showCancelModal)
      .toBe(false);


    expect(component.reservationToCancel)
      .toBeNull();


    expect(component.successMessage)
      .toBe(
        'Prenotazione annullata correttamente.'
      );


    // =================================================
    // GET DI AGGIORNAMENTO LISTA
    // =================================================

    const getRequest =
      httpTestingController.expectOne(
        'http://localhost:8080/api/reservations/my'
      );


    expect(getRequest.request.method)
      .toBe('GET');


    getRequest.flush([]);


    expect(component.reservations)
      .toEqual([]);


    expect(component.successMessage)
      .toBe(
        'Prenotazione annullata correttamente.'
      );

  });


  // =================================================
  // ERRORE CANCELLAZIONE
  // =================================================

  it('should handle error while cancelling reservation', () => {

    component.reservationToCancel =
      confirmedReservation;


    component.showCancelModal =
      true;


    component.cancelReservation();


    expect(component.cancelLoading)
      .toBe(true);


    const request =
      httpTestingController.expectOne(
        'http://localhost:8080/api/reservations/1'
      );


    expect(request.request.method)
      .toBe('DELETE');


    request.flush(

      'Error',

      {
        status: 500,
        statusText: 'Internal Server Error'
      }

    );


    expect(component.cancelLoading)
      .toBe(false);


    expect(component.errorMessage)
      .toBe(
        'Non è stato possibile annullare la prenotazione.'
      );


    expect(component.showCancelModal)
      .toBe(true);


    expect(component.reservationToCancel)
      .toEqual(
        confirmedReservation
      );

  });


  // =================================================
  // CHIUSURA FINESTRA
  // =================================================

  it('should emit close event when closing window', () => {

    const emitSpy =
      vi.spyOn(
        component.close,
        'emit'
      );


    component.cancelLoading =
      false;


    component.closeWindow();


    expect(emitSpy)
      .toHaveBeenCalled();

  });


  // =================================================
  // NON CHIUDERE DURANTE CANCELLAZIONE
  // =================================================

  it('should not emit close event while cancellation is loading', () => {

    const emitSpy =
      vi.spyOn(
        component.close,
        'emit'
      );


    component.cancelLoading =
      true;


    component.closeWindow();


    expect(emitSpy)
      .not.toHaveBeenCalled();

  });


  // =================================================
  // TRACK BY
  // =================================================

  it('should return reservation id for trackBy', () => {

    expect(
      component.trackByReservationId(
        0,
        confirmedReservation
      )
    )
      .toBe(1);


    expect(
      component.trackByReservationId(
        1,
        confirmedReservation2
      )
    )
      .toBe(2);

  });

});