import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SlicePipe } from '@angular/common';

import { ReservationFormComponent } from '../reservation-form/reservation-form';


@Component({
    selector: 'app-booking',
    standalone: true,

    imports: [
        FormsModule,
        ReservationFormComponent,
        SlicePipe
    ],

    templateUrl: './booking.html',
    styleUrl: './booking.css'
})
export class BookingComponent {


    // =========================
    // DATI PRENOTAZIONE
    // =========================

    reservationDate = '';

    startTime = '';


    // Serve per mostrare
    // gli errori del primo form

    bookingSubmitted = false;


    // =========================
    // POPUP / MODALE
    // =========================

    showReservation = false;

    reservationSuccess = false;

    reservationError = false;


    // =========================
    // DATA MINIMA
    // =========================

    minDate = '';


    // =========================
    // ORARI DISPONIBILI
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


    filteredTimes = [
        ...this.availableTimes
    ];


    // =========================
    // PRENOTAZIONE CONFERMATA
    // =========================

    confirmedReservation: any = null;


    // =========================
    // COSTRUTTORE
    // =========================

    constructor() {

        const today = new Date();

        this.minDate =
            today
                .toISOString()
                .split('T')[0];

    }


    // =========================
    // CAMBIO DATA
    // =========================

    onDateChange() {

        const today = new Date();

        const selected =
            new Date(this.reservationDate);


        // Se è stata selezionata oggi

        if (
            selected.toDateString()
            ===
            today.toDateString()
        ) {


            const currentHour =
                today.getHours();


            this.filteredTimes =
                this.availableTimes.filter(time => {


                    const hour =
                        Number(
                            time.split(':')[0]
                        );


                    // Mostra solo gli orari
                    // successivi all'ora corrente

                    return hour > currentHour;

                });


        }

        // Se è un giorno futuro

        else {

            this.filteredTimes = [
                ...this.availableTimes
            ];

        }


        // Se l'orario precedentemente
        // selezionato non è più disponibile

        if (
            !this.filteredTimes.includes(this.startTime)
        ) {

            this.startTime = '';

        }

    }


    // =========================
    // CONTINUA
    // =========================

    openReservation() {


        // Mostriamo gli errori

        this.bookingSubmitted = true;


        // Se manca data oppure orario
        // non apriamo il modale

        if (
            !this.reservationDate ||
            !this.startTime
        ) {

            return;

        }


        // Tutto corretto

        this.showReservation = true;

    }


    // =========================
    // CHIUDI MODALE
    // =========================

    closeModal() {

        this.showReservation = false;

    }


    // =========================
    // PRENOTAZIONE COMPLETATA
    // =========================

    closeReservation(reservation: any) {

        this.showReservation = false;

        this.confirmedReservation =
            reservation;

        this.reservationSuccess = true;

    }


    // =========================
    // ERRORE BACKEND
    // =========================

    showError() {

        this.showReservation = false;

        this.reservationError = true;

    }


    // =========================
    // CHIUDI ERRORE
    // =========================

    closeError() {

        this.reservationError = false;

    }


    // =========================
    // CHIUDI SUCCESSO
    // =========================

    closeSuccess() {

        this.reservationSuccess = false;

        this.reservationDate = '';

        this.startTime = '';

        this.bookingSubmitted = false;


        // Ripristiniamo tutti gli orari

        this.filteredTimes = [
            ...this.availableTimes
        ];

    }

}