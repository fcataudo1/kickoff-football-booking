import {
    Component,
    inject,
    OnInit,
    OnDestroy,
    ChangeDetectorRef,
    output
} from '@angular/core';

import { FormsModule } from '@angular/forms';

import { Subscription } from 'rxjs';

import { AuthService } from '../../services/auth.service';

import { ReservationService } from '../../services/reservation.service';

import { Reservation } from '../../models/reservation';

import { ReservationRequest } from '../../models/reservation-request';


@Component({
    selector: 'app-booking',

    standalone: true,

    imports: [
        FormsModule
    ],

    templateUrl: './booking.html',

    styleUrl: './booking.css'
})
export class BookingComponent
    implements OnInit, OnDestroy {


    // =========================
    // SERVIZI
    // =========================

    private readonly authService =
        inject(AuthService);

    private readonly reservationService =
        inject(ReservationService);

    private readonly changeDetector =
        inject(ChangeDetectorRef);


    // =========================
    // EVENTI
    // =========================

    openLogin =
        output<void>();

    openRegister =
        output<void>();


    // =========================
    // SUBSCRIPTION
    // =========================

    private authSubscription?: Subscription;


    // =========================
    // AUTENTICAZIONE
    // =========================

    isLoggedIn = false;


    // =========================
    // RUOLO
    // =========================

    userRole = '';


    // =========================
    // CLIENTE
    // =========================

    get isCliente(): boolean {

        return this.userRole === 'CLIENTE';

    }


    // =========================
    // RECEPTIONIST
    // =========================

    get isReceptionist(): boolean {

        return this.userRole === 'RECEPTIONIST';

    }


    // =========================
    // ADMIN
    // =========================

    get isAdmin(): boolean {

        return this.userRole === 'ADMIN';

    }


    // =========================
    // PRENOTAZIONE
    // =========================

    reservationDate = '';

    startTime = '';


    // =========================
    // ERRORI FORM
    // =========================

    dateErrorMessage = '';

    timeErrorMessage = '';


    // =========================
    // DATA MINIMA
    // =========================

    minDate = '';


    // =========================
    // ORARI
    // =========================

    availableTimes = [
        '16:00',
        '17:00',
        '18:00',
        '19:00',
        '20:00',
        '21:00',
        '22:00',
        '23:00'
    ];


    // =========================
    // STATO
    // =========================

    loading = false;

    reservationSuccess = false;

    reservationError = false;

    reservationErrorMessage = '';

    confirmedReservation:
        Reservation | null = null;


    // =========================
    // INIT
    // =========================

    ngOnInit(): void {

        this.updateAuthState();


        this.authSubscription =
            this.authService.loggedIn$
                .subscribe(() => {

                    this.updateAuthState();

                    this.changeDetector.detectChanges();

                });


        const today = new Date();

        this.minDate =
            today
                .toISOString()
                .split('T')[0];

    }


    // =========================
    // AGGIORNA STATO AUTH
    // =========================

    private updateAuthState(): void {

        this.isLoggedIn =
            this.authService.isLoggedIn();


        const user =
            this.authService.getUser();


        this.userRole =
            user?.ruolo ?? '';

    }


    // =========================
    // DESTROY
    // =========================

    ngOnDestroy(): void {

        this.authSubscription?.unsubscribe();

    }


    // =========================
    // PRENOTA
    // =========================

    openBooking(): void {

        if (this.loading) {

            return;

        }


        // =========================
        // RESET ERRORI
        // =========================

        this.dateErrorMessage = '';

        this.timeErrorMessage = '';

        this.reservationSuccess = false;

        this.reservationError = false;

        this.reservationErrorMessage = '';

        this.confirmedReservation = null;


        // =========================
        // AUTENTICAZIONE
        // =========================

        if (!this.isLoggedIn) {

            this.reservationErrorMessage =
                'Devi effettuare l’accesso prima di poter prenotare.';

            this.reservationError = true;

            this.changeDetector.detectChanges();

            return;

        }


        // =========================
        // CONTROLLO RUOLO
        // =========================

        if (!this.isCliente) {

            this.reservationErrorMessage =
                'La prenotazione online è riservata ai clienti.';

            this.reservationError = true;

            this.changeDetector.detectChanges();

            return;

        }


        // =========================
        // DATA + ORARIO MANCANTI
        // =========================

        if (
            !this.reservationDate &&
            !this.startTime
        ) {

            this.dateErrorMessage =
                'Seleziona una data.';

            this.timeErrorMessage =
                'Seleziona un orario.';

            this.changeDetector.detectChanges();

            return;

        }


        // =========================
        // DATA MANCANTE
        // =========================

        if (!this.reservationDate) {

            this.dateErrorMessage =
                'Seleziona una data.';

            this.changeDetector.detectChanges();

            return;

        }


        // =========================
        // DATA NON VALIDA
        // =========================

        if (
            this.reservationDate < this.minDate
        ) {

            this.dateErrorMessage =
                'Seleziona una data valida.';

            this.changeDetector.detectChanges();

            return;

        }


        // =========================
        // ORARIO MANCANTE
        // =========================

        if (!this.startTime) {

            this.timeErrorMessage =
                'Seleziona un orario.';

            this.changeDetector.detectChanges();

            return;

        }


        // =========================
        // REQUEST
        // =========================

        const request: ReservationRequest = {

            reservationDate:
                this.reservationDate,

            startTime:
                this.startTime

        };


        // =========================
        // LOADING
        // =========================

        this.loading = true;

        this.changeDetector.detectChanges();


        // =========================
        // BACKEND
        // =========================

        this.reservationService
            .create(request)
            .subscribe({

                // =========================
                // SUCCESSO
                // =========================

                next: (reservation: Reservation) => {

                    console.log(
                        'PRENOTAZIONE CREATA:',
                        reservation
                    );


                    this.loading = false;


                    this.confirmedReservation =
                        reservation;


                    this.reservationSuccess =
                        true;


                    this.reservationDate = '';

                    this.startTime = '';


                    this.changeDetector.detectChanges();

                },


                // =========================
                // ERRORE
                // =========================

                error: error => {

                    console.error(
                        'ERRORE PRENOTAZIONE:',
                        error
                    );


                    this.loading = false;


                    this.reservationErrorMessage =
                        this.getReservationErrorMessage(
                            error
                        );


                    this.reservationError =
                        true;


                    this.changeDetector.detectChanges();

                }

            });

    }


    // =========================
    // MESSAGGIO ERRORE BACKEND
    // =========================

    private getReservationErrorMessage(
        error: any
    ): string {

        const backendMessage =
            error?.error?.message
                ?.toLowerCase() ?? '';


        if (
            backendMessage.includes(
                'already have a reservation'
            ) ||
            backendMessage.includes(
                'reservation for this time'
            )
        ) {

            return 'Hai già una prenotazione per questa data e questo orario. Prova a scegliere un altro orario.';

        }


        if (
            backendMessage.includes(
                'no field available'
            ) ||
            backendMessage.includes(
                'field is not available'
            ) ||
            backendMessage.includes(
                'no available field'
            )
        ) {

            return 'Non ci sono campi disponibili per la data e l’orario selezionati. Prova a scegliere un altro orario.';

        }


        return 'Non è stato possibile completare la prenotazione. Riprova tra qualche momento.';

    }


    // =========================
    // CHIUDI SUCCESSO
    // =========================

    closeSuccess(): void {

        this.reservationSuccess = false;

        this.changeDetector.detectChanges();

    }


    // =========================
    // CHIUDI ERRORE
    // =========================

    closeError(): void {

        this.reservationError = false;

        this.reservationErrorMessage = '';

        this.changeDetector.detectChanges();

    }

}