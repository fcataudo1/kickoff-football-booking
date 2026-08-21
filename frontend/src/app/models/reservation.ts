export type ReservationStatus =
  'CONFIRMED' | 'CANCELLED';


export interface FootballField {

  id: number;

  name: string;

  active: boolean;

}


export interface UserResponse {

  id: number;

  nome: string;

  cognome: string;

  email: string;

  telefono: string;

  ruolo: string;

}


export interface Reservation {

  id: number;

  reservationDate: string;

  startTime: string;

  status: ReservationStatus;

  price: number;

  footballField: FootballField;

  user: UserResponse;

}