import {
  Component,
  inject,
  output,
  OnInit,
  OnDestroy,
  ChangeDetectorRef
} from '@angular/core';

import { Subscription } from 'rxjs';

import { AuthService } from '../../services/auth.service';


@Component({
  selector: 'app-navbar',
  standalone: true,
  imports: [],
  templateUrl: './navbar.html',
  styleUrl: './navbar.css'
})
export class NavbarComponent
  implements OnInit, OnDestroy {


  // ==============================
  // AUTH SERVICE
  // ==============================

  private readonly authService =
    inject(AuthService);


  // ==============================
  // CHANGE DETECTOR
  // ==============================

  private readonly changeDetector =
    inject(ChangeDetectorRef);


  // ==============================
  // SUBSCRIPTION
  // ==============================

  private authSubscription?: Subscription;


  // ==============================
  // EVENTI
  // ==============================

  loginClick =
    output<void>();


  registerClick =
    output<void>();


  myReservationsClick =
    output<void>();


  receptionistReservationsClick =
    output<void>();

  adminUsersClick = output<void>();


  profileClick =
    output<void>();


  // ==============================
  // STATO AUTENTICAZIONE
  // ==============================

  isLoggedIn = false;


  // ==============================
  // POPUP LOGOUT
  // ==============================

  showLogoutConfirm = false;


  // ==============================
  // INIT
  // ==============================

  ngOnInit(): void {

    this.updateAuthState();


    this.authSubscription =
      this.authService.loggedIn$
        .subscribe(() => {

          this.updateAuthState();

          this.changeDetector.markForCheck();

        });

  }


  // ==============================
  // DESTROY
  // ==============================

  ngOnDestroy(): void {

    this.authSubscription?.unsubscribe();

  }


  // ==============================
  // AGGIORNA STATO
  // ==============================

  private updateAuthState(): void {

    this.isLoggedIn =
      this.authService.isLoggedIn();

  }


  // ==============================
  // UTENTE
  // ==============================

  get userName(): string {

    const user =
      this.authService.getUser();

    return user
      ? user.nome
      : '';

  }


  // ==============================
  // RUOLO
  // ==============================

  get userRole(): string {

    const user =
      this.authService.getUser();

    return user?.ruolo ?? '';

  }


  // ==============================
  // CLIENTE
  // ==============================

  get isCliente(): boolean {

    return this.userRole === 'CLIENTE';

  }


  // ==============================
  // RECEPTIONIST
  // ==============================

  get isReceptionist(): boolean {

    return this.userRole === 'RECEPTIONIST';

  }


  // ==============================
  // ADMIN
  // ==============================

  get isAdmin(): boolean {

    return this.userRole === 'ADMIN';

  }


  // ==============================
  // ETICHETTA RUOLO
  // ==============================

  get roleLabel(): string {

    switch (this.userRole) {

      case 'CLIENTE':
        return 'Cliente';

      case 'RECEPTIONIST':
        return 'Receptionist';

      case 'ADMIN':
        return 'Amministratore';

      default:
        return 'Utente';

    }

  }


  // ==============================
  // LOGIN
  // ==============================

  openLogin(): void {

    this.loginClick.emit();

  }


  // ==============================
  // REGISTRAZIONE
  // ==============================

  openRegister(): void {

    this.registerClick.emit();

  }


  // ==============================
  // PROFILO
  // ==============================

  openProfile(): void {

    this.profileClick.emit();

  }


  // ==============================
  // MIE PRENOTAZIONI
  // ==============================

  openMyReservations(): void {

    this.myReservationsClick.emit();

  }


  // ==============================
  // PRENOTAZIONI RECEPTIONIST
  // ==============================

  openReceptionistReservations(): void {

    this.receptionistReservationsClick.emit();

  }


  // ==============================
  // APRI POPUP LOGOUT
  // ==============================

  openLogoutConfirm(): void {

    this.showLogoutConfirm = true;

    this.changeDetector.markForCheck();

  }


  // ==============================
  // ANNULLA LOGOUT
  // ==============================

  cancelLogout(): void {

    this.showLogoutConfirm = false;

    this.changeDetector.markForCheck();

  }


  // ==============================
  // CONFERMA LOGOUT
  // ==============================

  confirmLogout(): void {

    this.showLogoutConfirm = false;

    this.authService.logout();

    // Aggiornamento immediato
    this.updateAuthState();

    this.changeDetector.markForCheck();

  }

  openAdminUsers(): void {

    this.adminUsersClick.emit();

}

}