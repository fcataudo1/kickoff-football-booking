import {
  ChangeDetectorRef,
  Component,
  inject,
  output
} from '@angular/core';

import { FormsModule } from '@angular/forms';

import { AuthService } from '../../services/auth.service';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './login.html',
  styleUrl: './login.css'
})
export class LoginComponent {

  private readonly authService = inject(AuthService);
  private readonly changeDetector = inject(ChangeDetectorRef);

  close = output<void>();

  email = '';
  password = '';

  errorMessage = '';
  loading = false;
  showPassword = false;

  touched = {
    email: false,
    password: false
  };


  isEmailValid(): boolean {

    const email = this.email.trim();

    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);
  }


  isPasswordValid(): boolean {

    return this.password.trim().length > 0;
  }


  touch(field: 'email' | 'password'): void {

    this.touched[field] = true;
  }


  shouldShowError(
    field: 'email' | 'password'
  ): boolean {

    if (!this.touched[field]) {
      return false;
    }

    return field === 'email'
      ? !this.isEmailValid()
      : !this.isPasswordValid();
  }


  shouldShowValid(
    field: 'email' | 'password'
  ): boolean {

    if (!this.touched[field]) {
      return false;
    }

    return field === 'email'
      ? this.isEmailValid()
      : this.isPasswordValid();
  }


  togglePassword(): void {

    this.showPassword = !this.showPassword;
  }


  isFormValid(): boolean {

    return (
      this.isEmailValid() &&
      this.isPasswordValid()
    );
  }


  login(): void {

    this.errorMessage = '';

    this.touched.email = true;
    this.touched.password = true;

    if (!this.isFormValid()) {

      this.errorMessage =
        'Controlla i dati inseriti nel modulo.';

      return;
    }

    this.loading = true;

    this.authService
      .login({
        email: this.email.trim(),
        password: this.password
      })
      .subscribe({

        next: user => {

          console.log(
            'Login effettuato:',
            user
          );

          this.loading = false;

          this.close.emit();
        },

        error: error => {

          console.error(
            'Errore login:',
            error
          );

          this.loading = false;

          this.errorMessage =
            error.status === 400 ||
            error.status === 401
              ? 'Email o password non valide.'
              : 'Errore durante il login. Riprova più tardi.';

          this.changeDetector.detectChanges();
        }

      });
  }


  closeLogin(): void {

    if (!this.loading) {
      this.close.emit();
    }
  }


  onOverlayClick(event: MouseEvent): void {

    if (
      event.target === event.currentTarget &&
      !this.loading
    ) {
      this.close.emit();
    }
  }

}