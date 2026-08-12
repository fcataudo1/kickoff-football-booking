import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Reservation } from '../models/reservation';


@Injectable({
  providedIn: 'root'
})
export class ReservationService {


  private apiUrl = 'http://localhost:8080/api/reservations';



  constructor(
    private http: HttpClient
  ) {}



  create(reservation: Reservation) {

    return this.http.post<Reservation>(
      this.apiUrl,
      reservation
    );

  }

}