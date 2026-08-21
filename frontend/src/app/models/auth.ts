export interface LoginRequest {
  email: string;
  password: string;
}

export interface LoginResponse {
  id: number;
  nome: string;
  cognome: string;
  email: string;
  telefono: string;
  ruolo: 'CLIENTE' | 'RECEPTIONIST' | 'ADMIN';
  token: string;
}