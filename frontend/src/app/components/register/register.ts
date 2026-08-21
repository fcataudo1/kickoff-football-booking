import { Component, inject, output, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';

import { AuthService } from '../../services/auth.service';
import { RegisterRequest } from '../../models/register';

@Component({
  selector: 'app-register',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './register.html',
  styleUrl: './register.css'
})
export class RegisterComponent {

  private readonly authService = inject(AuthService);


  // =================================================
  // EVENTI
  // =================================================

  close = output<void>();

  login = output<void>();


  // =================================================
  // CAMPI FORM
  // =================================================

  nome = '';

  cognome = '';

  email = '';

  telefono = '';

  password = '';

  confirmPassword = '';


  // =================================================
  // STATO
  // =================================================

  loading = false;

  errorMessage = '';

  successMessage = '';


  // Popup account già esistente
  showAlreadyRegistered = signal(false);


  // Popup registrazione completata
  showRegistrationSuccess = signal(false);


  // =================================================
  // CAMPI TOCCATI
  // =================================================

  touched = {

    nome: false,

    cognome: false,

    email: false,

    telefono: false,

    password: false,

    confirmPassword: false

  };


  // =================================================
  // PASSWORD
  // =================================================

  showPassword = false;

  showConfirmPassword = false;


  // =================================================
  // VALIDAZIONE NOME
  // =================================================

  isNomeValid(): boolean {

    const value = this.nome.trim();

    return (
      value.length >= 2 &&
      /^[a-zA-ZÀ-ÖØ-öø-ÿ\s']+$/.test(value)
    );

  }


  // =================================================
  // VALIDAZIONE COGNOME
  // =================================================

  isCognomeValid(): boolean {

    const value = this.cognome.trim();

    return (
      value.length >= 2 &&
      /^[a-zA-ZÀ-ÖØ-öø-ÿ\s']+$/.test(value)
    );

  }


  // =================================================
  // VALIDAZIONE EMAIL
  // =================================================

  isEmailValid(): boolean {

    const value = this.email.trim();

    const emailRegex =
      /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

    return emailRegex.test(value);

  }


  // =================================================
  // VALIDAZIONE TELEFONO
  // =================================================

  isTelefonoValid(): boolean {

    const value = this.telefono.trim();

    return /^\d{10}$/.test(value);

  }


  // =================================================
  // VALIDAZIONE PASSWORD
  // =================================================

  hasUppercase(): boolean {

    return /[A-Z]/.test(this.password);

  }


  hasLowercase(): boolean {

    return /[a-z]/.test(this.password);

  }


  hasNumber(): boolean {

    return /\d/.test(this.password);

  }


  hasMinimumLength(): boolean {

    return this.password.length >= 8;

  }


  isPasswordValid(): boolean {

    return (
      this.hasMinimumLength() &&
      this.hasUppercase() &&
      this.hasLowercase() &&
      this.hasNumber()
    );

  }


  // =================================================
  // CONFERMA PASSWORD
  // =================================================

  isConfirmPasswordValid(): boolean {

    return (
      this.confirmPassword.length > 0 &&
      this.password === this.confirmPassword
    );

  }


  // =================================================
  // TOUCH CAMPI
  // =================================================

  touch(
    field:
      | 'nome'
      | 'cognome'
      | 'email'
      | 'telefono'
      | 'password'
      | 'confirmPassword'
  ): void {

    this.touched[field] = true;

  }


  // =================================================
  // MOSTRA ERRORE
  // =================================================

  shouldShowError(
    field:
      | 'nome'
      | 'cognome'
      | 'email'
      | 'telefono'
      | 'password'
      | 'confirmPassword'
  ): boolean {

    if (!this.touched[field]) {

      return false;

    }


    switch (field) {

      case 'nome':
        return !this.isNomeValid();

      case 'cognome':
        return !this.isCognomeValid();

      case 'email':
        return !this.isEmailValid();

      case 'telefono':
        return !this.isTelefonoValid();

      case 'password':
        return !this.isPasswordValid();

      case 'confirmPassword':
        return !this.isConfirmPasswordValid();

      default:
        return false;

    }

  }


  // =================================================
  // MOSTRA CAMPO VALIDO
  // =================================================

  shouldShowValid(
    field:
      | 'nome'
      | 'cognome'
      | 'email'
      | 'telefono'
      | 'password'
      | 'confirmPassword'
  ): boolean {

    if (!this.touched[field]) {

      return false;

    }


    switch (field) {

      case 'nome':
        return this.isNomeValid();

      case 'cognome':
        return this.isCognomeValid();

      case 'email':
        return this.isEmailValid();

      case 'telefono':
        return this.isTelefonoValid();

      case 'password':
        return this.isPasswordValid();

      case 'confirmPassword':
        return this.isConfirmPasswordValid();

      default:
        return false;

    }

  }


  // =================================================
  // MOSTRA / NASCONDI PASSWORD
  // =================================================

  togglePassword(): void {

    this.showPassword =
      !this.showPassword;

  }


  toggleConfirmPassword(): void {

    this.showConfirmPassword =
      !this.showConfirmPassword;

  }


  // =================================================
  // VALIDAZIONE COMPLETA
  // =================================================

  isFormValid(): boolean {

    return (

      this.isNomeValid() &&

      this.isCognomeValid() &&

      this.isEmailValid() &&

      this.isTelefonoValid() &&

      this.isPasswordValid() &&

      this.isConfirmPasswordValid()

    );

  }


  // =================================================
  // REGISTRAZIONE
  // =================================================

  register(): void {

    // Reset messaggi
    this.errorMessage = '';
    this.successMessage = '';

    // Chiude eventuali popup precedenti
    this.showAlreadyRegistered.set(false);
    this.showRegistrationSuccess.set(false);


    // Tutti i campi diventano touched
    this.touched.nome = true;
    this.touched.cognome = true;
    this.touched.email = true;
    this.touched.telefono = true;
    this.touched.password = true;
    this.touched.confirmPassword = true;


    // =================================================
    // CONTROLLO FORM
    // =================================================

    if (!this.isFormValid()) {

      this.errorMessage =
        'Controlla i dati inseriti nel modulo.';

      return;

    }


    // =================================================
    // LOADING
    // =================================================

    this.loading = true;


    // =================================================
    // DATI DA INVIARE
    // =================================================

    const data: RegisterRequest = {

      nome: this.nome.trim(),

      cognome: this.cognome.trim(),

      email: this.email.trim(),

      telefono: this.telefono.trim(),

      password: this.password

    };


    // =================================================
    // CHIAMATA BACKEND
    // =================================================

    this.authService
      .register(data)
      .subscribe({

        // =================================================
        // SUCCESSO
        // =================================================

        next: user => {

          console.log(
            'Registrazione effettuata:',
            user
          );


          // Ferma lo spinner
          this.loading = false;


          // Mostra popup successo
          this.showRegistrationSuccess.set(true);


          // =================================================
          // PULIZIA FORM
          // =================================================

          this.nome = '';

          this.cognome = '';

          this.email = '';

          this.telefono = '';

          this.password = '';

          this.confirmPassword = '';


          // Reset validazioni
          this.touched = {

            nome: false,

            cognome: false,

            email: false,

            telefono: false,

            password: false,

            confirmPassword: false

          };


          // Reset occhi password
          this.showPassword = false;

          this.showConfirmPassword = false;

        },


        // =================================================
        // ERRORE
        // =================================================

        error: error => {

          console.error(
            'Errore registrazione:',
            error
          );


          // IMPORTANTISSIMO:
          // lo spinner viene sempre fermato
          this.loading = false;


          // =================================================
          // 409
          // ACCOUNT GIÀ ESISTENTE
          // =================================================

          if (error.status === 409) {

            this.showAlreadyRegistered.set(true);

            return;

          }


          // =================================================
          // 400
          // DATI NON VALIDI
          // =================================================

          if (error.status === 400) {

            this.errorMessage =
              'I dati inseriti non sono validi.';

            return;

          }


          // =================================================
          // ALTRI ERRORI
          // =================================================

          this.errorMessage =
            'Errore durante la registrazione. Riprova più tardi.';

        }

      });

  }


  // =================================================
  // POPUP ACCOUNT GIÀ ESISTENTE
  // =================================================

  closeAlreadyRegistered(): void {

    this.showAlreadyRegistered.set(false);

    this.close.emit();

  }


  // =================================================
  // DAL POPUP ACCOUNT ESISTENTE → LOGIN
  // =================================================

  goToLogin(): void {

    this.showAlreadyRegistered.set(false);

    this.close.emit();

    this.login.emit();

  }


  // =================================================
  // POPUP REGISTRAZIONE COMPLETATA
  // =================================================

  closeRegistrationSuccess(): void {

    this.showRegistrationSuccess.set(false);

    this.close.emit();

  }


  // =================================================
  // REGISTRAZIONE COMPLETATA → LOGIN
  // =================================================

  goToLoginAfterRegistration(): void {

    this.showRegistrationSuccess.set(false);

    this.close.emit();

    this.login.emit();

  }


  // =================================================
  // CHIUSURA REGISTRAZIONE
  // =================================================

  closeRegister(): void {

    if (this.loading) {

      return;

    }

    this.close.emit();

  }

}