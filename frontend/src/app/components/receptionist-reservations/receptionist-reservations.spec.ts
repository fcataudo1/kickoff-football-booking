import {
  ComponentFixture,
  TestBed
} from '@angular/core/testing';

import { of, throwError } from 'rxjs';

import { ReceptionistReservationsComponent } from './receptionist-reservations';

import { HttpClient } from '@angular/common/http';

import { Reservation } from '../../models/reservation';


describe('ReceptionistReservationsComponent', () => {

  let component: ReceptionistReservationsComponent;
  let fixture: ComponentFixture<ReceptionistReservationsComponent>;

  let httpMock: {
    get: ReturnType<typeof vi.fn>;
    patch: ReturnType<typeof vi.fn>;
  };


  // =================================================
  // PRENOTAZIONI MOCK
  // =================================================

  const reservation1: Reservation = {

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


  const reservation2: Reservation = {

    id: 2,

    reservationDate: '2026-08-24',

    startTime: '20:00',

    status: 'CONFIRMED',

    price: 50,

    footballField: {
      id: 2,
      name: 'Campo 2',
      active: true
    },

    user: {
      id: 2,
      nome: 'Luigi',
      cognome: 'Verdi',
      email: 'luigi@email.it',
      telefono: '3337654321',
      ruolo: 'CLIENTE'
    }

  };


  const cancelledReservation: Reservation = {

    id: 3,

    reservationDate: '2026-08-26',

    startTime: '19:00',

    status: 'CANCELLED',

    price: 50,

    footballField: {
      id: 1,
      name: 'Campo 1',
      active: true
    },

    user: {
      id: 3,
      nome: 'Anna',
      cognome: 'Bianchi',
      email: 'anna@email.it',
      telefono: '3331112222',
      ruolo: 'CLIENTE'
    }

  };


  // =================================================
  // BEFORE EACH
  // =================================================

  beforeEach(async () => {

    httpMock = {

      get: vi.fn(),

      patch: vi.fn()

    };


    httpMock.get
      .mockReturnValue(
        of([])
      );


    await TestBed.configureTestingModule({

      imports: [
        ReceptionistReservationsComponent
      ],

      providers: [

        {
          provide: HttpClient,
          useValue: httpMock
        }

      ]

    }).compileComponents();


    fixture =
      TestBed.createComponent(
        ReceptionistReservationsComponent
      );

    component =
      fixture.componentInstance;

  });


  // =================================================
  // CREAZIONE COMPONENTE
  // =================================================

  it('should create', () => {

    expect(component)
      .toBeTruthy();

  });


  // =================================================
  // INIT
  // =================================================

  it('should load reservations on init', () => {

    httpMock.get
      .mockReturnValue(
        of([
          reservation1,
          reservation2
        ])
      );


    component.ngOnInit();


    expect(httpMock.get)
      .toHaveBeenCalledWith(
        'http://localhost:8080/api/reservations'
      );


    expect(component.reservations)
      .toEqual([
        reservation1,
        reservation2
      ]);


    expect(component.loading)
      .toBe(false);

  });


  // =================================================
  // ERRORE CARICAMENTO
  // =================================================

  it('should handle loading error', () => {

    httpMock.get
      .mockReturnValue(

        throwError(() => ({
          status: 500
        }))

      );


    component.loadReservations();


    expect(component.loading)
      .toBe(false);


    expect(component.errorMessage)
      .toBe(
        'Non è stato possibile caricare le prenotazioni.'
      );

  });


  // =================================================
  // DESTROY
  // =================================================

  it('should unsubscribe on destroy', () => {

    component.ngOnInit();


    const subscription =
      component['requestSubscription'];


    expect(subscription)
      .toBeTruthy();


    component.ngOnDestroy();


    expect(subscription?.closed)
      .toBe(true);

  });


  // =================================================
  // APRI POPUP CANCELLAZIONE
  // =================================================

  it('should open cancellation modal for confirmed reservation', () => {

    component.cancelReservation(
      reservation1
    );


    expect(component.showCancelModal)
      .toBe(true);


    expect(component.reservationToCancel)
      .toEqual(reservation1);


    expect(component.errorMessage)
      .toBe('');

  });


  // =================================================
  // NON APRIRE POPUP PRENOTAZIONE ANNULLATA
  // =================================================

  it('should not open cancellation modal for cancelled reservation', () => {

    component.showCancelModal = false;

    component.reservationToCancel = null;


    component.cancelReservation(
      cancelledReservation
    );


    expect(component.showCancelModal)
      .toBe(false);


    expect(component.reservationToCancel)
      .toBeNull();

  });


  // =================================================
  // BLOCCA SE CANCELLAMENTO IN CORSO
  // =================================================

  it('should not open cancellation modal while another cancellation is in progress', () => {

    component.cancellingId = 10;


    component.cancelReservation(
      reservation1
    );


    expect(component.showCancelModal)
      .toBe(false);


    expect(component.reservationToCancel)
      .toBeNull();

  });


  // =================================================
  // CHIUDI POPUP
  // =================================================

  it('should close cancellation modal', () => {

    component.showCancelModal = true;

    component.reservationToCancel =
      reservation1;


    component.closeCancelModal();


    expect(component.showCancelModal)
      .toBe(false);


    expect(component.reservationToCancel)
      .toBeNull();

  });


  // =================================================
  // NON CHIUDERE POPUP DURANTE CANCELLAZIONE
  // =================================================

  it('should not close cancellation modal while cancelling', () => {

    component.showCancelModal = true;

    component.reservationToCancel =
      reservation1;

    component.cancellingId =
      reservation1.id;


    component.closeCancelModal();


    expect(component.showCancelModal)
      .toBe(true);


    expect(component.reservationToCancel)
      .toEqual(reservation1);

  });


  // =================================================
  // CONFERMA CANCELLAZIONE SENZA PRENOTAZIONE
  // =================================================

  it('should do nothing when there is no reservation to cancel', () => {

    component.reservationToCancel =
      null;


    component.confirmCancellation();


    expect(httpMock.patch)
      .not.toHaveBeenCalled();

  });


  // =================================================
  // NON CANCELLARE SE GIÀ IN CORSO
  // =================================================

  it('should not start another cancellation while one is in progress', () => {

    component.reservationToCancel =
      reservation1;

    component.cancellingId =
      reservation2.id;


    component.confirmCancellation();


    expect(httpMock.patch)
      .not.toHaveBeenCalled();

  });


  // =================================================
  // CANCELLAZIONE RIUSCITA
  // =================================================

  it('should cancel reservation successfully', () => {

    component.reservations = [
      reservation1,
      reservation2
    ];

    component.reservationToCancel =
      reservation1;


    httpMock.patch
      .mockReturnValue(
        of(undefined)
      );


    component.confirmCancellation();


    expect(httpMock.patch)
      .toHaveBeenCalledWith(

        'http://localhost:8080/api/reservations/1/cancel',

        {}

      );


    expect(component.cancellingId)
      .toBeNull();


    expect(component.showCancelModal)
      .toBe(false);


    expect(component.reservationToCancel)
      .toBeNull();


    expect(component.reservations[0].status)
      .toBe('CANCELLED');

  });


  // =================================================
  // ERRORE CANCELLAZIONE
  // =================================================

  it('should handle cancellation error', () => {

    component.reservationToCancel =
      reservation1;


    httpMock.patch
      .mockReturnValue(

        throwError(() => ({
          status: 500
        }))

      );


    component.confirmCancellation();


    expect(component.cancellingId)
      .toBeNull();


    expect(component.errorMessage)
      .toBe(
        'Non è stato possibile annullare la prenotazione.'
      );

  });


  // =================================================
  // FILTRO DATA
  // =================================================

  it('should filter reservations by date', () => {

    component.reservations = [
      reservation1,
      reservation2
    ];


    component.selectedDate =
      '2026-08-25';


    expect(component.filteredReservations)
      .toEqual([
        reservation1
      ]);

  });


  // =================================================
  // FILTRO CAMPO
  // =================================================

  it('should filter reservations by field', () => {

    component.reservations = [
      reservation1,
      reservation2
    ];


    component.selectedFieldId =
      '2';


    expect(component.filteredReservations)
      .toEqual([
        reservation2
      ]);

  });


  // =================================================
  // FILTRO DATA + CAMPO
  // =================================================

  it('should apply date and field filters together', () => {

    component.reservations = [
      reservation1,
      reservation2
    ];


    component.selectedDate =
      '2026-08-25';

    component.selectedFieldId =
      '1';


    expect(component.filteredReservations)
      .toEqual([
        reservation1
      ]);

  });


  // =================================================
  // ORDINAMENTO
  // =================================================

  it('should sort filtered reservations by date and time', () => {

    component.reservations = [

      reservation1,

      reservation2

    ];


    const result =
      component.filteredReservations;


    expect(result[0])
      .toEqual(reservation2);


    expect(result[1])
      .toEqual(reservation1);

  });


  // =================================================
  // CAMPI UNICI
  // =================================================

  it('should return unique fields', () => {

    component.reservations = [

      reservation1,

      reservation2,

      cancelledReservation

    ];


    const fields =
      component.fields;


    expect(fields.length)
      .toBe(2);


    expect(
      fields[0].footballField.id
    )
      .toBe(1);


    expect(
      fields[1].footballField.id
    )
      .toBe(2);

  });


  // =================================================
  // FORMATTA DATA
  // =================================================

  it('should format date in Italian format', () => {

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
  // FORMATTA ORA
  // =================================================

  it('should format time correctly', () => {

    expect(
      component.formatTime(
        '18:00:00'
      )
    )
      .toBe('18:00');

  });


  // =================================================
  // STATO CONFERMATO
  // =================================================

  it('should identify confirmed reservations', () => {

    expect(
      component.isConfirmed(
        reservation1
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
  // STATO ANNULLATO
  // =================================================

  it('should identify cancelled reservations', () => {

    expect(
      component.isCancelled(
        cancelledReservation
      )
    )
      .toBe(true);


    expect(
      component.isCancelled(
        reservation1
      )
    )
      .toBe(false);

  });


  // =================================================
  // RESET FILTRI
  // =================================================

  it('should reset filters', () => {

    component.selectedDate =
      '2026-08-25';

    component.selectedFieldId =
      '2';


    component.resetFilters();


    expect(component.selectedDate)
      .toBe('');


    expect(component.selectedFieldId)
      .toBe('');

  });


  // =================================================
  // CHIUDI FINESTRA
  // =================================================

  it('should emit close event', () => {

    const closeSpy =
      vi.spyOn(
        component.close,
        'emit'
      );


    component.loading = false;

    component.showCancelModal = false;


    component.closeWindow();


    expect(closeSpy)
      .toHaveBeenCalled();

  });


  // =================================================
  // NON CHIUDERE DURANTE LOADING
  // =================================================

  it('should not emit close while loading', () => {

    const closeSpy =
      vi.spyOn(
        component.close,
        'emit'
      );


    component.loading =
      true;


    component.closeWindow();


    expect(closeSpy)
      .not.toHaveBeenCalled();

  });


  // =================================================
  // NON CHIUDERE CON MODALE APERTO
  // =================================================

  it('should not emit close while cancellation modal is open', () => {

    const closeSpy =
      vi.spyOn(
        component.close,
        'emit'
      );


    component.loading = false;

    component.showCancelModal = true;


    component.closeWindow();


    expect(closeSpy)
      .not.toHaveBeenCalled();

  });


  // =================================================
  // TRACK BY
  // =================================================

  it('should return reservation id for trackBy', () => {

    expect(
      component.trackByReservationId(
        0,
        reservation1
      )
    )
      .toBe(1);

  });

});