import {
    ComponentFixture,
    TestBed
} from '@angular/core/testing';

import {
    BehaviorSubject,
    of,
    throwError
} from 'rxjs';

import {
    describe,
    it,
    expect,
    beforeEach,
    afterEach,
    vi
} from 'vitest';

import { BookingComponent } from './booking';

import { AuthService } from '../../services/auth.service';
import { ReservationService } from '../../services/reservation.service';

import { Reservation } from '../../models/reservation';


describe('BookingComponent', () => {

    let component: BookingComponent;
    let fixture: ComponentFixture<BookingComponent>;

    let authServiceMock: {
        isLoggedIn: ReturnType<typeof vi.fn>;
        getUser: ReturnType<typeof vi.fn>;
        loggedIn$: BehaviorSubject<boolean>;
    };

    let reservationServiceMock: {
        create: ReturnType<typeof vi.fn>;
    };

    let loggedInSubject: BehaviorSubject<boolean>;


    // =================================================
    // MOCK PRENOTAZIONE
    // =================================================

    const mockReservation: Reservation = {

        id: 1,

        reservationDate:
            '2026-08-25',

        startTime:
            '18:00',

        status:
            'CONFIRMED',

        price:
            50,

        footballField: {

            id: 1,

            name:
                'Campo 1',

            active:
                true

        },

        user: {

            id: 1,

            nome:
                'Mario',

            cognome:
                'Rossi',

            email:
                'mario@email.it',

            telefono:
                '3331234567',

            ruolo:
                'CLIENTE'

        }

    };


    // =================================================
    // SETUP
    // =================================================

    beforeEach(async () => {

        loggedInSubject =
            new BehaviorSubject<boolean>(
                false
            );


        authServiceMock = {

            isLoggedIn:
                vi.fn(),

            getUser:
                vi.fn(),

            loggedIn$:
                loggedInSubject

        };


        reservationServiceMock = {

            create:
                vi.fn()

        };


        await TestBed.configureTestingModule({

            imports: [

                BookingComponent

            ],

            providers: [

                {
                    provide:
                        AuthService,

                    useValue:
                        authServiceMock

                },

                {
                    provide:
                        ReservationService,

                    useValue:
                        reservationServiceMock

                }

            ]

        }).compileComponents();


        fixture =
            TestBed.createComponent(
                BookingComponent
            );

        component =
            fixture.componentInstance;

    });


    // =================================================
    // CLEANUP
    // =================================================

    afterEach(() => {

        loggedInSubject.complete();

    });


    // =================================================
    // CREAZIONE COMPONENTE
    // =================================================

    it('deve creare il componente', () => {

        expect(component)
            .toBeTruthy();

    });


    // =================================================
    // INIT
    // =================================================

    it('deve inizializzare lo stato di autenticazione', () => {

        authServiceMock.isLoggedIn
            .mockReturnValue(true);


        authServiceMock.getUser
            .mockReturnValue({

                id: 1,

                nome:
                    'Mario',

                cognome:
                    'Rossi',

                email:
                    'mario@email.it',

                telefono:
                    '3331234567',

                ruolo:
                    'CLIENTE'

            });


        component.ngOnInit();


        expect(component.isLoggedIn)
            .toBe(true);


        expect(component.userRole)
            .toBe('CLIENTE');


        expect(component.minDate)
            .toBeTruthy();

    });


    // =================================================
    // RUOLO CLIENTE
    // =================================================

    it('deve identificare correttamente il ruolo CLIENTE', () => {

        component.userRole =
            'CLIENTE';


        expect(component.isCliente)
            .toBe(true);

        expect(component.isReceptionist)
            .toBe(false);

        expect(component.isAdmin)
            .toBe(false);

    });


    // =================================================
    // RUOLO RECEPTIONIST
    // =================================================

    it('deve identificare correttamente il ruolo RECEPTIONIST', () => {

        component.userRole =
            'RECEPTIONIST';


        expect(component.isCliente)
            .toBe(false);

        expect(component.isReceptionist)
            .toBe(true);

        expect(component.isAdmin)
            .toBe(false);

    });


    // =================================================
    // RUOLO ADMIN
    // =================================================

    it('deve identificare correttamente il ruolo ADMIN', () => {

        component.userRole =
            'ADMIN';


        expect(component.isCliente)
            .toBe(false);

        expect(component.isReceptionist)
            .toBe(false);

        expect(component.isAdmin)
            .toBe(true);

    });


    // =================================================
    // UTENTE NON AUTENTICATO
    // =================================================

    it('deve rifiutare la prenotazione per un utente non autenticato', () => {

        component.isLoggedIn =
            false;


        component.openBooking();


        expect(component.reservationError)
            .toBe(true);


        expect(
            component.reservationErrorMessage
        ).toBe(
            'Devi effettuare l’accesso prima di poter prenotare.'
        );


        expect(
            reservationServiceMock.create
        ).not.toHaveBeenCalled();

    });


    // =================================================
    // RUOLO NON CLIENTE
    // =================================================

    it('deve rifiutare la prenotazione per un utente non cliente', () => {

        component.isLoggedIn =
            true;

        component.userRole =
            'ADMIN';


        component.openBooking();


        expect(component.reservationError)
            .toBe(true);


        expect(
            component.reservationErrorMessage
        ).toBe(
            'La prenotazione online è riservata ai clienti.'
        );


        expect(
            reservationServiceMock.create
        ).not.toHaveBeenCalled();

    });


    // =================================================
    // DATA E ORARIO MANCANTI
    // =================================================

    it('deve richiedere data e orario', () => {

        component.isLoggedIn =
            true;

        component.userRole =
            'CLIENTE';

        component.reservationDate =
            '';

        component.startTime =
            '';


        component.openBooking();


        expect(
            component.dateErrorMessage
        ).toBe(
            'Seleziona una data.'
        );


        expect(
            component.timeErrorMessage
        ).toBe(
            'Seleziona un orario.'
        );


        expect(
            reservationServiceMock.create
        ).not.toHaveBeenCalled();

    });


    // =================================================
    // DATA MANCANTE
    // =================================================

    it('deve richiedere una data di prenotazione', () => {

        component.isLoggedIn =
            true;

        component.userRole =
            'CLIENTE';

        component.reservationDate =
            '';

        component.startTime =
            '18:00';


        component.openBooking();


        expect(
            component.dateErrorMessage
        ).toBe(
            'Seleziona una data.'
        );


        expect(
            reservationServiceMock.create
        ).not.toHaveBeenCalled();

    });


    // =================================================
    // DATA PASSATA
    // =================================================

    it('deve rifiutare una data precedente alla data minima', () => {

        component.isLoggedIn =
            true;

        component.userRole =
            'CLIENTE';

        component.minDate =
            '2026-08-20';

        component.reservationDate =
            '2026-08-19';

        component.startTime =
            '18:00';


        component.openBooking();


        expect(
            component.dateErrorMessage
        ).toBe(
            'Seleziona una data valida.'
        );


        expect(
            reservationServiceMock.create
        ).not.toHaveBeenCalled();

    });


    // =================================================
    // ORARIO MANCANTE
    // =================================================

    it('deve richiedere un orario di prenotazione', () => {

        component.isLoggedIn =
            true;

        component.userRole =
            'CLIENTE';

        component.reservationDate =
            '2026-08-25';

        component.startTime =
            '';


        component.openBooking();


        expect(
            component.timeErrorMessage
        ).toBe(
            'Seleziona un orario.'
        );


        expect(
            reservationServiceMock.create
        ).not.toHaveBeenCalled();

    });


    // =================================================
    // PRENOTAZIONE RIUSCITA
    // =================================================

    it('deve creare correttamente una prenotazione', () => {

        component.isLoggedIn =
            true;

        component.userRole =
            'CLIENTE';

        component.reservationDate =
            '2026-08-25';

        component.startTime =
            '18:00';


        reservationServiceMock.create
            .mockReturnValue(
                of(mockReservation)
            );


        component.openBooking();


        expect(
            reservationServiceMock.create
        ).toHaveBeenCalledWith({

            reservationDate:
                '2026-08-25',

            startTime:
                '18:00'

        });


        expect(component.loading)
            .toBe(false);


        expect(component.reservationSuccess)
            .toBe(true);


        expect(component.confirmedReservation)
            .toEqual(
                mockReservation
            );


        expect(component.reservationDate)
            .toBe('');


        expect(component.startTime)
            .toBe('');

    });


    // =================================================
    // ERRORE PRENOTAZIONE GIÀ ESISTENTE
    // =================================================

    it('deve mostrare errore per una prenotazione già esistente', () => {

        component.isLoggedIn =
            true;

        component.userRole =
            'CLIENTE';

        component.reservationDate =
            '2026-08-25';

        component.startTime =
            '18:00';


        reservationServiceMock.create
            .mockReturnValue(

                throwError(() => ({

                    error: {

                        message:
                            'You already have a reservation'

                    }

                }))

            );


        component.openBooking();


        expect(component.loading)
            .toBe(false);


        expect(component.reservationError)
            .toBe(true);


        expect(
            component.reservationErrorMessage
        ).toBe(
            'Hai già una prenotazione per questa data e questo orario. Prova a scegliere un altro orario.'
        );

    });


    // =================================================
    // ERRORE CAMPO NON DISPONIBILE
    // =================================================

    it('deve mostrare errore quando non ci sono campi disponibili', () => {

        component.isLoggedIn =
            true;

        component.userRole =
            'CLIENTE';

        component.reservationDate =
            '2026-08-25';

        component.startTime =
            '18:00';


        reservationServiceMock.create
            .mockReturnValue(

                throwError(() => ({

                    error: {

                        message:
                            'No field available'

                    }

                }))

            );


        component.openBooking();


        expect(component.reservationError)
            .toBe(true);


        expect(
            component.reservationErrorMessage
        ).toBe(
            'Non ci sono campi disponibili per la data e l’orario selezionati. Prova a scegliere un altro orario.'
        );

    });


    // =================================================
    // ERRORE GENERICO
    // =================================================

    it('deve mostrare un errore generico per un errore inatteso', () => {

        component.isLoggedIn =
            true;

        component.userRole =
            'CLIENTE';

        component.reservationDate =
            '2026-08-25';

        component.startTime =
            '18:00';


        reservationServiceMock.create
            .mockReturnValue(

                throwError(() => ({

                    error: {

                        message:
                            'Unexpected error'

                    }

                }))

            );


        component.openBooking();


        expect(component.reservationError)
            .toBe(true);


        expect(
            component.reservationErrorMessage
        ).toBe(
            'Non è stato possibile completare la prenotazione. Riprova tra qualche momento.'
        );

    });


    // =================================================
    // CHIUDI SUCCESSO
    // =================================================

    it('deve chiudere il messaggio di successo', () => {

        component.reservationSuccess =
            true;


        component.closeSuccess();


        expect(component.reservationSuccess)
            .toBe(false);

    });


    // =================================================
    // CHIUDI ERRORE
    // =================================================

    it('deve chiudere il messaggio di errore', () => {

        component.reservationError =
            true;

        component.reservationErrorMessage =
            'Errore';


        component.closeError();


        expect(component.reservationError)
            .toBe(false);


        expect(
            component.reservationErrorMessage
        ).toBe('');

    });


    // =================================================
    // NON PRENOTA DURANTE LOADING
    // =================================================

    it('non deve iniziare una nuova prenotazione durante il loading', () => {

        component.loading =
            true;


        component.openBooking();


        expect(
            reservationServiceMock.create
        ).not.toHaveBeenCalled();

    });


    // =================================================
    // DISTRUZIONE
    // =================================================

    it('deve annullare la subscription alla distruzione del componente', () => {

        component.ngOnInit();


        const subscription =
            component['authSubscription'];


        expect(subscription)
            .toBeTruthy();


        component.ngOnDestroy();


        expect(subscription?.closed)
            .toBe(true);

    });

});