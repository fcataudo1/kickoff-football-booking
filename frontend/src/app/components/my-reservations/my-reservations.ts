import {
  Component,
  ChangeDetectorRef,
  EventEmitter,
  OnInit,
  Output,
  inject
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
export class MyReservationsComponent implements OnInit {

  private readonly http = inject(HttpClient);

  private readonly cdr = inject(ChangeDetectorRef);

  @Output()
  close = new EventEmitter<void>();

  private readonly apiUrl =
    'http://localhost:8080/api/reservations';

  reservations: Reservation[] = [];

  // =========================
  // LOADING
  // =========================

  initialLoading = false;

  cancelLoading = false;

  // =========================
  // MESSAGGI
  // =========================

  errorMessage = '';

  successMessage = '';

  // =========================
  // MODALE CANCELLAZIONE
  // =========================

  showCancelModal = false;

  reservationToCancel: Reservation | null = null;


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

    if (
      this.initialLoading ||
      this.cancelLoading
    ) {
      return;
    }

    this.close.emit();

  }


  // =========================
  // CARICA PRENOTAZIONI
  // =========================

  loadReservations(): void {

    this.initialLoading = true;

    this.errorMessage = '';

    this.http
      .get<Reservation[]>(
        `${this.apiUrl}/my`
      )
      .subscribe({

        next: reservations => {

          this.reservations = reservations;

          this.initialLoading = false;

          /*
           * Forza Angular ad aggiornare
           * immediatamente la view.
           */
          this.cdr.detectChanges();

        },

        error: error => {

          console.error(
            'Errore caricamento prenotazioni:',
            error
          );

          this.initialLoading = false;

          this.errorMessage =
            'Non è stato possibile caricare le tue prenotazioni.';

          this.cdr.detectChanges();

        }

      });

  }


  // =========================
  // PROSSIME PRENOTAZIONI
  // =========================

  get upcomingReservations(): Reservation[] {

    const today = new Date();

    today.setHours(
      0,
      0,
      0,
      0
    );

    return this.reservations

      .filter(reservation => {

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

        return reservationDate >= today;

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

  get pastReservations(): Reservation[] {

    const today = new Date();

    today.setHours(
      0,
      0,
      0,
      0
    );

    return this.reservations

      .filter(reservation => {

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

        return reservationDate < today;

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
  // APRI MODALE
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

    this.cdr.detectChanges();

  }


  // =========================
  // CHIUDI MODALE
  // =========================

  closeCancelModal(): void {

    if (this.cancelLoading) {
      return;
    }

    this.showCancelModal =
      false;

    this.reservationToCancel =
      null;

    this.cdr.detectChanges();

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

    this.cancelLoading = true;

    this.errorMessage = '';

    this.successMessage = '';

    this.http
      .delete(
        `${this.apiUrl}/${id}`
      )
      .subscribe({

        next: () => {

          this.cancelLoading = false;

          this.showCancelModal =
            false;

          this.reservationToCancel =
            null;

          this.successMessage =
            'Prenotazione annullata correttamente.';

          /*
           * Ricarica la lista senza
           * riattivare lo spinner iniziale.
           */
          this.http
            .get<Reservation[]>(
              `${this.apiUrl}/my`
            )
            .subscribe({

              next: reservations => {

                this.reservations =
                  reservations;

                this.cdr.detectChanges();

              },

              error: error => {

                console.error(
                  'Errore aggiornamento prenotazioni:',
                  error
                );

                this.cdr.detectChanges();

              }

            });

          this.cdr.detectChanges();

        },

        error: error => {

          console.error(
            'Errore annullamento:',
            error
          );

          this.cancelLoading = false;

          this.errorMessage =
            'Non è stato possibile annullare la prenotazione.';

          this.cdr.detectChanges();

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