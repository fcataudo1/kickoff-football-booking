import {
  Component,
  HostListener
} from '@angular/core';

import { NavbarComponent } from '../navbar/navbar';
import { HeroComponent } from '../hero/hero';
import { BookingComponent } from '../booking/booking';
import { ServicesComponent } from '../services/services';
import { VideoSectionComponent } from '../video-section/video-section';
import { WhyKickoffComponent } from '../why-kickoff/why-kickoff';
import { StatsComponent } from '../stats/stats';
import { ContactComponent } from '../contact/contact';
import { FooterComponent } from '../footer/footer';

import { LoginComponent } from '../login/login';
import { RegisterComponent } from '../register/register';

import { MyReservationsComponent } from '../my-reservations/my-reservations';
import {
  ReceptionistReservationsComponent
} from '../receptionist-reservations/receptionist-reservations';

import { ProfileComponent } from '../profile/profile';

import { AdminUsersComponent } from '../admin-users/admin-users';


@Component({
  selector: 'app-home',
  standalone: true,

  imports: [
    NavbarComponent,
    HeroComponent,
    BookingComponent,
    ServicesComponent,
    VideoSectionComponent,
    WhyKickoffComponent,
    StatsComponent,
    ContactComponent,
    FooterComponent,

    LoginComponent,
    RegisterComponent,

    MyReservationsComponent,
    ReceptionistReservationsComponent,
    ProfileComponent,

    AdminUsersComponent
  ],

  templateUrl: './home.html',
  styleUrl: './home.css'
})
export class HomeComponent {

  // =========================
  // STATO POPUP
  // =========================

  showLogin = false;

  showRegister = false;

  showMyReservations = false;

  showReceptionistReservations = false;

  showAdminUsers = false;

  showProfile = false;

  showBackToTop = false;


  // =========================
  // LOGIN
  // =========================

  openLogin(): void {

    this.closeAllModals();

    this.showLogin = true;

  }


  closeLogin(): void {

    this.showLogin = false;

  }


  // =========================
  // REGISTRAZIONE
  // =========================

  openRegister(): void {

    this.closeAllModals();

    this.showRegister = true;

  }


  closeRegister(): void {

    this.showRegister = false;

  }


  // =========================
  // LOGIN DALLA REGISTRAZIONE
  // =========================

  openLoginFromRegister(): void {

    this.closeAllModals();

    this.showLogin = true;

  }


  // =========================
  // MIE PRENOTAZIONI
  // =========================

  openMyReservations(): void {

    this.closeAllModals();

    this.showMyReservations = true;

  }


  closeMyReservations(): void {

    this.showMyReservations = false;

  }


  // =========================
  // PRENOTAZIONI RECEPTIONIST
  // =========================

  openReceptionistReservations(): void {

    this.closeAllModals();

    this.showReceptionistReservations = true;

  }


  closeReceptionistReservations(): void {

    this.showReceptionistReservations = false;

  }


  // =========================
  // GESTIONE UTENTI ADMIN
  // =========================

  openAdminUsers(): void {

    this.closeAllModals();

    this.showAdminUsers = true;

  }


  closeAdminUsers(): void {

    this.showAdminUsers = false;

  }


  // =========================
  // PROFILO
  // =========================

  openProfile(): void {

    this.closeAllModals();

    this.showProfile = true;

  }


  closeProfile(): void {

    this.showProfile = false;

  }


  // =========================
  // CHIUDI TUTTI I POPUP
  // =========================

  private closeAllModals(): void {

    this.showLogin = false;

    this.showRegister = false;

    this.showMyReservations = false;

    this.showReceptionistReservations = false;

    this.showAdminUsers = false;

    this.showProfile = false;

  }

  // =========================
// PULSANTE TORNA SU
// =========================



@HostListener('window:scroll')
onWindowScroll(): void {

  this.showBackToTop =
    window.scrollY > 300;

}

scrollToTop(): void {

  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  });

}

}