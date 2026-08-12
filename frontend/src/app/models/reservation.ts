export type ReservationStatus = 'CONFIRMED' | 'CANCELLED';

export interface Reservation {
  id?: number;

  customerName: string;
  customerPhone: string;
  customerEmail: string;

  reservationDate: string;
  startTime: string;

  status?: ReservationStatus;
  price?: number;

  footballField?: {
    id: number;
    name: string;
  };
}