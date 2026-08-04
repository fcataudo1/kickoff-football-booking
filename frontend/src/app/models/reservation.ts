export interface Reservation {

    id?: number;

    customerName: string;

    customerPhone: string;

    customerEmail: string;

    reservationDate: string;

    startTime: string;

    status?: string;

    price?: number;

    footballField?: {

        id: number;

        name: string;

    };

}