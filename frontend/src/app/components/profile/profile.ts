import {
  Component,
  inject,
  output
} from '@angular/core';

import { AuthService } from '../../services/auth.service';


@Component({
  selector: 'app-profile',
  standalone: true,
  imports: [],
  templateUrl: './profile.html',
  styleUrl: './profile.css'
})
export class ProfileComponent {


  // ==============================
  // AUTH SERVICE
  // ==============================

  private readonly authService =
    inject(AuthService);


  // ==============================
  // EVENTO CHIUSURA
  // ==============================

  close =
    output<void>();


  // ==============================
  // UTENTE
  // ==============================

  get user() {

    return this.authService.getUser();

  }


  // ==============================
  // NOME COMPLETO
  // ==============================

  get fullName(): string {

    const user = this.user;

    if (!user) {
      return '';
    }

    return `${user.nome} ${user.cognome}`;

  }


  // ==============================
  // RUOLO
  // ==============================

  get roleLabel(): string {

    switch (this.user?.ruolo) {

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
  // CHIUDI
  // ==============================

  closeWindow(): void {

    this.close.emit();

  }

}