import {
  Component,
  inject,
  OnInit,
  OnDestroy,
  Output,
  EventEmitter,
  ChangeDetectorRef
} from '@angular/core';

import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { Subscription } from 'rxjs';

import { Reservation } from '../../models/reservation';


@Component({
  selector: 'app-receptionist-reservations',

  standalone: true,

  imports: [
    CommonModule,
    FormsModule
  ],

  templateUrl: './receptionist-reservations.html',

  styleUrl: './receptionist-reservations.css'
})
export class ReceptionistReservationsComponent
  implements OnInit, OnDestroy {


  // =========================
  // HTTP
  // =========================

  private readonly http =
    inject(HttpClient);


  // =========================
  // CHANGE DETECTOR
  // =========================

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

  cancellingId: number | null = null;

  private requestSubscription?:
    Subscription;


  // =========================
  // POPUP CONFERMA
  // =========================

  showCancelModal = false;

  reservationToCancel:
    Reservation | null = null;


  // =========================
  // FILTRI
  // =========================

  selectedDate = '';

  selectedFieldId = '';


  // =========================
  // INIT
  // =========================

  ngOnInit(): void {

    this.loadReservations();

  }


  // =========================
  // DESTROY
  // =========================

  ngOnDestroy(): void {

    this.requestSubscription?.unsubscribe();

  }


  // =========================
  // CARICA PRENOTAZIONI
  // =========================

  loadReservations(): void {

    this.loading = true;

    this.errorMessage = '';


    this.requestSubscription =
      this.http
        .get<Reservation[]>(
          this.apiUrl
        )
        .subscribe({

          next: reservations => {

            console.log(
              'PRENOTAZIONI RECEPTIONIST:',
              reservations
            );

            this.reservations =
              reservations;

            this.loading =
              false;

            this.changeDetector.detectChanges();

          },


          error: error => {

            console.error(
              'Errore caricamento prenotazioni receptionist:',
              error
            );

            this.loading =
              false;

            this.errorMessage =
              'Non è stato possibile caricare le prenotazioni.';

            this.changeDetector.detectChanges();

          }

        });

  }


  // =========================
  // APRE POPUP ANNULLAMENTO
  // =========================

  cancelReservation(
    reservation: Reservation
  ): void {

    if (
      this.cancellingId !== null
    ) {

      return;

    }


    if (
      this.isCancelled(reservation)
    ) {

      return;

    }


    this.reservationToCancel =
      reservation;

    this.showCancelModal =
      true;

    this.errorMessage = '';

    this.changeDetector.detectChanges();

  }


  // =========================
  // CHIUDE POPUP
  // =========================

  closeCancelModal(): void {

    if (
      this.cancellingId !== null
    ) {

      return;

    }


    this.showCancelModal =
      false;

    this.reservationToCancel =
      null;

  }


  // =========================
  // CONFERMA ANNULLAMENTO
  // =========================

  confirmCancellation(): void {

    if (
      !this.reservationToCancel
    ) {

      return;

    }


    if (
      this.cancellingId !== null
    ) {

      return;

    }


    const reservation =
      this.reservationToCancel;


    this.cancellingId =
      reservation.id;

    this.errorMessage = '';


    // =========================
    // CHIAMATA BACKEND
    // =========================

    this.http
      .patch<void>(
        `${this.apiUrl}/${reservation.id}/cancel`,
        {}
      )
      .subscribe({

        // =========================
        // SUCCESSO
        // =========================

        next: () => {

          console.log(
            'PRENOTAZIONE ANNULLATA:',
            reservation.id
          );


          // Trasforma la prenotazione
          // da CONFIRMED a CANCELLED

          this.reservations =
            this.reservations.map(
              item => {

                if (
                  item.id ===
                  reservation.id
                ) {

                  return {
                    ...item,
                    status: 'CANCELLED'
                  };

                }

                return item;

              }
            );


          this.cancellingId =
            null;

          this.showCancelModal =
            false;

          this.reservationToCancel =
            null;


          this.changeDetector.detectChanges();

        },


        // =========================
        // ERRORE
        // =========================

        error: error => {

          console.error(
            'Errore annullamento prenotazione:',
            error
          );


          this.cancellingId =
            null;

          this.errorMessage =
            'Non è stato possibile annullare la prenotazione.';

          this.changeDetector.detectChanges();

        }

      });

  }


  // =========================
  // PRENOTAZIONI FILTRATE
  // =========================

  get filteredReservations():
    Reservation[] {

    return this.reservations

      .filter(reservation => {

        if (
          this.selectedDate &&
          reservation.reservationDate !==
          this.selectedDate
        ) {

          return false;

        }


        if (
          this.selectedFieldId &&
          reservation.footballField.id
            .toString() !==
          this.selectedFieldId
        ) {

          return false;

        }


        return true;

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
  // CAMPI DISPONIBILI
  // =========================

  get fields(): Reservation[] {

    const uniqueFields =
      new Map<number, Reservation>();


    for (
      const reservation
      of this.reservations
    ) {

      if (
        !uniqueFields.has(
          reservation.footballField.id
        )
      ) {

        uniqueFields.set(
          reservation.footballField.id,
          reservation
        );

      }

    }


    return Array.from(
      uniqueFields.values()
    );

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
  // STATO
  // =========================

  isConfirmed(
    reservation: Reservation
  ): boolean {

    return reservation.status ===
      'CONFIRMED';

  }


  isCancelled(
    reservation: Reservation
  ): boolean {

    return reservation.status ===
      'CANCELLED';

  }


  // =========================
  // RESET FILTRI
  // =========================

  resetFilters(): void {

    this.selectedDate = '';

    this.selectedFieldId = '';

    this.changeDetector.detectChanges();

  }


  // =========================
  // CHIUDI FINESTRA
  // =========================

  closeWindow(): void {

    if (this.loading) {

      return;

    }


    if (this.showCancelModal) {

      return;

    }


    this.close.emit();

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