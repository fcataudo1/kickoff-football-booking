import {
  Component,
  inject,
  OnInit,
  Output,
  EventEmitter,
  ChangeDetectorRef
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';

import { Reservation } from '../../models/reservation';


@Component({
  selector: 'app-my-reservations',
  standalone: true,
  imports: [
    CommonModule
  ],
  templateUrl: './my-reservations.html',
  styleUrl: './my-reservations.css'
})
export class MyReservationsComponent
  implements OnInit {


  // =========================
  // SERVIZI
  // =========================

  private readonly http =
    inject(HttpClient);

  private readonly changeDetector =
    inject(ChangeDetectorRef);


  // =========================
  // EVENTO CHIUSURA
  // =========================

  @Output()
  close =
    new EventEmitter<void>();


  // =========================
  // API
  // =========================

  private readonly apiUrl =
    'http://localhost:8080/api/reservations';


  // =========================
  // STATO
  // =========================

  reservations: Reservation[] = [];

  loading = false;

  errorMessage = '';

  successMessage = '';


  // =========================
  // MODALE CANCELLAZIONE
  // =========================

  showCancelModal = false;

  reservationToCancel:
    Reservation | null = null;


  // =========================
  // INIT
  // =========================

  ngOnInit(): void {

    this.loadReservations();

  }


  // =========================
  // CHIUDI FINESTRA
  // =========================

  closeWindow(): void {

    if (this.loading) {
      return;
    }

    this.close.emit();

  }


  // =========================
  // CARICA PRENOTAZIONI
  // =========================

  loadReservations(): void {

    this.loading = true;

    this.errorMessage = '';

    this.successMessage = '';


    this.http
      .get<Reservation[]>(
        `${this.apiUrl}/my`
      )
      .subscribe({

        // =========================
        // SUCCESSO
        // =========================

        next: reservations => {

          console.log(
            'PRENOTAZIONI RICEVUTE:',
            reservations
          );


          this.reservations =
            reservations;


          this.loading =
            false;


          /*
           * Forza Angular ad aggiornare
           * la view dopo la risposta HTTP.
           */

          this.changeDetector.detectChanges();

        },


        // =========================
        // ERRORE
        // =========================

        error: error => {

          console.error(
            'Errore caricamento prenotazioni:',
            error
          );


          this.loading =
            false;


          this.errorMessage =
            'Non è stato possibile caricare le tue prenotazioni.';


          this.changeDetector.detectChanges();

        }

      });

  }


  // =========================
  // PRENOTAZIONI FUTURE
  // =========================

  get upcomingReservations():
    Reservation[] {

    const today =
      new Date();


    today.setHours(
      0,
      0,
      0,
      0
    );


    return this.reservations

      .filter(reservation => {

        /*
         * Le prenotazioni annullate
         * non sono considerate future.
         */

        if (
          reservation.status ===
          'CANCELLED'
        ) {

          return false;

        }


        const reservationDate =
          new Date(
            reservation.reservationDate
          );


        return reservationDate >=
          today;

      })

      .sort((a, b) => {

        return this
          .getReservationDateTime(a)
          .getTime()
          -
          this
          .getReservationDateTime(b)
          .getTime();

      });

  }


  // =========================
  // STORICO
  // =========================

  get pastReservations():
    Reservation[] {

    const today =
      new Date();


    today.setHours(
      0,
      0,
      0,
      0
    );


    return this.reservations

      .filter(reservation => {

        /*
         * Le prenotazioni annullate
         * finiscono sempre nello storico.
         */

        if (
          reservation.status ===
          'CANCELLED'
        ) {

          return true;

        }


        const reservationDate =
          new Date(
            reservation.reservationDate
          );


        return reservationDate <
          today;

      })

      .sort((a, b) => {

        return this
          .getReservationDateTime(b)
          .getTime()
          -
          this
          .getReservationDateTime(a)
          .getTime();

      });

  }


  // =========================
  // DATA + ORA
  // =========================

  private getReservationDateTime(
    reservation: Reservation
  ): Date {

    return new Date(
      `${reservation.reservationDate}T${reservation.startTime}`
    );

  }


  // =========================
  // DATA FORMATTATA
  // =========================

  formatDate(
    date: string
  ): string {

    const parsedDate =
      new Date(
        `${date}T00:00:00`
      );


    return parsedDate.toLocaleDateString(
      'it-IT',
      {
        day: '2-digit',
        month: 'long',
        year: 'numeric'
      }
    );

  }


  // =========================
  // ORA FORMATTATA
  // =========================

  formatTime(
    time: string
  ): string {

    return time.substring(
      0,
      5
    );

  }


  // =========================
  // STATO CONFERMATA
  // =========================

  isConfirmed(
    reservation: Reservation
  ): boolean {

    return reservation.status ===
      'CONFIRMED';

  }


  // =========================
  // STATO ANNULLATA
  // =========================

  isCancelled(
    reservation: Reservation
  ): boolean {

    return reservation.status ===
      'CANCELLED';

  }


  // =========================
  // APRI CONFERMA
  // =========================

  openCancelModal(
    reservation: Reservation
  ): void {

    if (
      reservation.status !==
      'CONFIRMED'
    ) {

      return;

    }


    this.reservationToCancel =
      reservation;


    this.showCancelModal =
      true;

  }


  // =========================
  // CHIUDI CONFERMA
  // =========================

  closeCancelModal(): void {

    if (this.loading) {
      return;
    }


    this.showCancelModal =
      false;


    this.reservationToCancel =
      null;

  }


  // =========================
  // CANCELLA PRENOTAZIONE
  // =========================

  cancelReservation(): void {

    if (
      !this.reservationToCancel
    ) {

      return;

    }


    const id =
      this.reservationToCancel.id;


    this.loading =
      true;


    this.errorMessage =
      '';

    this.successMessage =
      '';


    this.http
      .delete(
        `${this.apiUrl}/${id}`
      )
      .subscribe({

        // =========================
        // SUCCESSO
        // =========================

        next: () => {

          this.loading =
            false;


          this.showCancelModal =
            false;


          this.reservationToCancel =
            null;


          this.successMessage =
            'Prenotazione annullata correttamente.';


          this.changeDetector.detectChanges();


          /*
           * Ricarica la lista dopo
           * l'annullamento.
           */

          this.loadReservations();

        },


        // =========================
        // ERRORE
        // =========================

        error: error => {

          console.error(
            'Errore annullamento:',
            error
          );


          this.loading =
            false;


          this.errorMessage =
            'Non è stato possibile annullare la prenotazione.';


          this.changeDetector.detectChanges();

        }

      });

  }


  // =========================
  // TRACK BY
  // =========================

  trackByReservationId(
    index: number,
    reservation: Reservation
  ): number {

    return reservation.id;

  }

}