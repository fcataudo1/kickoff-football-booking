import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SlicePipe } from '@angular/common';

import { ReservationFormComponent } from '../reservation-form/reservation-form';


@Component({
selector:'app-booking',
standalone:true,
imports:[
 FormsModule,
 ReservationFormComponent,
 SlicePipe
],
templateUrl:'./booking.html',
styleUrl:'./booking.css'
})
export class BookingComponent {



reservationDate = '';

startTime = '';

showReservation = false;


reservationSuccess = false;

reservationError = false;


minDate = '';



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


confirmedReservation:any = null;




constructor(){


const today = new Date();


this.minDate =
today
.toISOString()
.split('T')[0];


}




onDateChange(){

const today = new Date();

const selected =
new Date(this.reservationDate);



if(
selected.toDateString()
===
today.toDateString()
){


const currentHour =
today.getHours();



this.filteredTimes =
this.availableTimes.filter(time=>{


const hour =
Number(
time.split(':')[0]
);


return hour > currentHour;


});


}
else{


this.filteredTimes =
[
...this.availableTimes
];


}


}




openReservation(){


if(
!this.reservationDate ||
!this.startTime
){

alert(
"Inserisci data e orario"
);

return;

}


this.showReservation=true;


}




closeModal(){

this.showReservation=false;

}





closeReservation(reservation:any){


this.showReservation=false;


this.confirmedReservation=reservation;


this.reservationSuccess=true;


}




showError(){


this.showReservation=false;

this.reservationError=true;


}



closeError(){

this.reservationError=false;

}



closeSuccess(){


this.reservationSuccess=false;


this.reservationDate='';

this.startTime='';


}



}