import { Component, Input, OnInit, OnChanges, SimpleChanges, Output, EventEmitter } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { ReservationService } from '../../services/reservation.service';
import { Reservation } from '../../models/reservation';


@Component({
  selector: 'app-reservation-form',
  standalone:true,
  imports:[
    FormsModule
  ],
  templateUrl:'./reservation-form.html',
  styleUrl:'./reservation-form.css'
})
export class ReservationFormComponent implements OnChanges {


  @Input()
  reservationDate!: string;


  @Input()
  startTime!: string;

  @Output()
  reservationCompleted = new EventEmitter<any>();

  @Output()
  reservationError = new EventEmitter<void>();


  reservation: Reservation = {

    customerName:'',
    customerPhone:'',
    customerEmail:'',
    reservationDate:'',
    startTime:''

  };



  constructor(
    private reservationService: ReservationService
  ){}



  ngOnChanges(changes: SimpleChanges){


    if(changes['reservationDate']){

      this.reservation.reservationDate =
      this.reservationDate;

    }


    if(changes['startTime']){

      this.reservation.startTime =
      this.startTime;

    }


  }




  createReservation(){


    if(
      !this.reservation.customerName ||
      !this.reservation.customerPhone ||
      !this.reservation.reservationDate ||
      !this.reservation.startTime
    ){

      alert("Compila tutti i campi");

      return;

    }



    this.reservationService
    .create(this.reservation)
    .subscribe({


      next:(response)=>{

      console.log(response);

      this.reservationCompleted.emit(response);

      },


      error:(err)=>{

        console.error(err);

        this.reservationError.emit();

      }


    });


  }


}