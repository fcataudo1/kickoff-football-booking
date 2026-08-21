export interface User {

    id: number;

    nome: string;

    cognome: string;

    email: string;

    telefono: string;

    ruolo: 'CLIENTE' | 'RECEPTIONIST' | 'ADMIN';

}