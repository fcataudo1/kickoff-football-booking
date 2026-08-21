import { Injectable, inject } from '@angular/core';

import {
    HttpClient
} from '@angular/common/http';

import {
    Observable
} from 'rxjs';

import {
    Reservation
} from '../models/reservation';

import {
    ReservationRequest
} from '../models/reservation-request';


@Injectable({
    providedIn: 'root'
})
export class ReservationService {

    private readonly http =
        inject(HttpClient);


    private readonly apiUrl =
        'http://localhost:8080/api/reservations';


    // =========================================
    // CREA PRENOTAZIONE
    // =========================================

    create(
        reservation: ReservationRequest
    ): Observable<Reservation> {

        return this.http.post<Reservation>(
            this.apiUrl,
            reservation
        );

    }


    // =========================================
    // MIE PRENOTAZIONI
    // =========================================

    getMyReservations():
        Observable<Reservation[]> {

        return this.http.get<Reservation[]>(
            `${this.apiUrl}/my`
        );

    }


    // =========================================
    // CANCELLA PRENOTAZIONE
    // =========================================

    cancel(
        id: number
    ): Observable<void> {

        return this.http.delete<void>(
            `${this.apiUrl}/${id}`
        );

    }

}