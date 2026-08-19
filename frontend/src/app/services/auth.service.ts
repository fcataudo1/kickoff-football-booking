import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import {
  Observable,
  tap,
  BehaviorSubject
} from 'rxjs';

import {
  LoginRequest,
  LoginResponse
} from '../models/auth';

import {
  RegisterRequest
} from '../models/register';


@Injectable({
  providedIn: 'root'
})
export class AuthService {

  private readonly http = inject(HttpClient);


  // =========================
  // API
  // =========================

  private readonly apiUrl =
    'http://localhost:8080/api/auth';


  // =========================
  // STATO AUTENTICAZIONE
  // =========================

  private readonly loggedInSubject =
    new BehaviorSubject<boolean>(
      !!localStorage.getItem('token')
    );


  readonly loggedIn$ =
    this.loggedInSubject.asObservable();


  // =========================
  // LOGIN
  // =========================

  login(
    credentials: LoginRequest
  ): Observable<LoginResponse> {

    return this.http
      .post<LoginResponse>(
        `${this.apiUrl}/login`,
        credentials
      )
      .pipe(

        tap(response => {

          localStorage.setItem(
            'token',
            response.token
          );

          localStorage.setItem(
            'user',
            JSON.stringify(response)
          );


          // Comunica al Navbar
          // che il login è avvenuto

          this.loggedInSubject.next(true);

        })

      );

  }


  // =========================
  // REGISTRAZIONE
  // =========================

  register(
    data: RegisterRequest
  ): Observable<LoginResponse> {

    return this.http.post<LoginResponse>(
      'http://localhost:8080/api/users',
      data
    );

  }


  // =========================
  // LOGOUT
  // =========================

  logout(): void {

    localStorage.removeItem('token');

    localStorage.removeItem('user');


    // Comunica al Navbar
    // che il logout è avvenuto

    this.loggedInSubject.next(false);

  }


  // =========================
  // TOKEN
  // =========================

  getToken(): string | null {

    return localStorage.getItem('token');

  }


  // =========================
  // UTENTE CORRENTE
  // =========================

  getUser(): LoginResponse | null {

    const user =
      localStorage.getItem('user');

    return user
      ? JSON.parse(user)
      : null;

  }


  // =========================
  // CONTROLLO LOGIN
  // =========================

  isLoggedIn(): boolean {

    return !!this.getToken();

  }


  // =========================
  // INFO UTENTE DAL BACKEND
  // =========================

  getCurrentUser(): Observable<LoginResponse> {

    return this.http.get<LoginResponse>(
      `${this.apiUrl}/me`
    );

  }

}