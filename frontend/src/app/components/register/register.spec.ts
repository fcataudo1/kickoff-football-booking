import {
  ComponentFixture,
  TestBed
} from '@angular/core/testing';

import { of, throwError } from 'rxjs';

import { RegisterComponent } from './register';

import { AuthService } from '../../services/auth.service';


describe('RegisterComponent', () => {

  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;

  let authServiceMock: {
    register: ReturnType<typeof vi.fn>;
  };


  // =================================================
  // MOCK UTENTE
  // =================================================

  const mockUser = {

    id: 1,

    nome: 'Mario',

    cognome: 'Rossi',

    email: 'mario@email.it',

    telefono: '3331234567',

    ruolo: 'CLIENTE'

  };


  // =================================================
  // BEFORE EACH
  // =================================================

  beforeEach(async () => {

    authServiceMock = {

      register: vi.fn()

    };


    await TestBed.configureTestingModule({

      imports: [
        RegisterComponent
      ],

      providers: [

        {
          provide: AuthService,
          useValue: authServiceMock
        }

      ]

    }).compileComponents();


    fixture =
      TestBed.createComponent(
        RegisterComponent
      );

    component =
      fixture.componentInstance;

  });


  // =================================================
  // CREAZIONE COMPONENTE
  // =================================================

  it('should create', () => {

    expect(component)
      .toBeTruthy();

  });


  // =================================================
  // VALIDAZIONE NOME
  // =================================================

  it('should validate nome correctly', () => {

    component.nome = 'Mario';

    expect(
      component.isNomeValid()
    )
      .toBe(true);


    component.nome = 'A';

    expect(
      component.isNomeValid()
    )
      .toBe(false);


    component.nome = 'Mario123';

    expect(
      component.isNomeValid()
    )
      .toBe(false);


    component.nome = 'Mario Rossi';

    expect(
      component.isNomeValid()
    )
      .toBe(true);


    component.nome = "D'Angelo";

    expect(
      component.isNomeValid()
    )
      .toBe(true);

  });


  // =================================================
  // VALIDAZIONE COGNOME
  // =================================================

  it('should validate cognome correctly', () => {

    component.cognome = 'Rossi';

    expect(
      component.isCognomeValid()
    )
      .toBe(true);


    component.cognome = 'R';

    expect(
      component.isCognomeValid()
    )
      .toBe(false);


    component.cognome = 'Rossi123';

    expect(
      component.isCognomeValid()
    )
      .toBe(false);

  });


  // =================================================
  // VALIDAZIONE EMAIL
  // =================================================

  it('should validate email correctly', () => {

    component.email =
      'mario@email.it';

    expect(
      component.isEmailValid()
    )
      .toBe(true);


    component.email =
      'mario@email';

    expect(
      component.isEmailValid()
    )
      .toBe(false);


    component.email =
      'marioemail.it';

    expect(
      component.isEmailValid()
    )
      .toBe(false);


    component.email =
      '';

    expect(
      component.isEmailValid()
    )
      .toBe(false);

  });


  // =================================================
  // VALIDAZIONE TELEFONO
  // =================================================

  it('should validate telefono correctly', () => {

    component.telefono =
      '3331234567';

    expect(
      component.isTelefonoValid()
    )
      .toBe(true);


    component.telefono =
      '333123456';

    expect(
      component.isTelefonoValid()
    )
      .toBe(false);


    component.telefono =
      '33312345678';

    expect(
      component.isTelefonoValid()
    )
      .toBe(false);


    component.telefono =
      '333123456a';

    expect(
      component.isTelefonoValid()
    )
      .toBe(false);

  });


  // =================================================
  // PASSWORD - MAIUSCOLA
  // =================================================

  it('should detect uppercase characters', () => {

    component.password =
      'Password1';

    expect(
      component.hasUppercase()
    )
      .toBe(true);


    component.password =
      'password1';

    expect(
      component.hasUppercase()
    )
      .toBe(false);

  });


  // =================================================
  // PASSWORD - MINUSCOLA
  // =================================================

  it('should detect lowercase characters', () => {

    component.password =
      'Password1';

    expect(
      component.hasLowercase()
    )
      .toBe(true);


    component.password =
      'PASSWORD1';

    expect(
      component.hasLowercase()
    )
      .toBe(false);

  });


  // =================================================
  // PASSWORD - NUMERO
  // =================================================

  it('should detect numbers', () => {

    component.password =
      'Password1';

    expect(
      component.hasNumber()
    )
      .toBe(true);


    component.password =
      'Password';

    expect(
      component.hasNumber()
    )
      .toBe(false);

  });


  // =================================================
  // PASSWORD - LUNGHEZZA
  // =================================================

  it('should validate minimum password length', () => {

    component.password =
      'Pass1234';

    expect(
      component.hasMinimumLength()
    )
      .toBe(true);


    component.password =
      'Pass123';

    expect(
      component.hasMinimumLength()
    )
      .toBe(false);

  });


  // =================================================
  // PASSWORD COMPLETA
  // =================================================

  it('should validate password correctly', () => {

    component.password =
      'Password1';

    expect(
      component.isPasswordValid()
    )
      .toBe(true);


    component.password =
      'password';

    expect(
      component.isPasswordValid()
    )
      .toBe(false);


    component.password =
      'PASSWORD1';

    expect(
      component.isPasswordValid()
    )
      .toBe(false);


    component.password =
      'Password';

    expect(
      component.isPasswordValid()
    )
      .toBe(false);

  });


  // =================================================
  // CONFERMA PASSWORD
  // =================================================

  it('should validate confirm password correctly', () => {

    component.password =
      'Password1';

    component.confirmPassword =
      'Password1';


    expect(
      component.isConfirmPasswordValid()
    )
      .toBe(true);


    component.confirmPassword =
      'Password2';


    expect(
      component.isConfirmPasswordValid()
    )
      .toBe(false);


    component.confirmPassword =
      '';


    expect(
      component.isConfirmPasswordValid()
    )
      .toBe(false);

  });


  // =================================================
  // TOUCH
  // =================================================

  it('should mark field as touched', () => {

    expect(
      component.touched.email
    )
      .toBe(false);


    component.touch('email');


    expect(
      component.touched.email
    )
      .toBe(true);

  });


  // =================================================
  // SHOULD SHOW ERROR
  // =================================================

  it('should show validation error only after field is touched', () => {

    component.email =
      'email-non-valida';


    expect(
      component.shouldShowError('email')
    )
      .toBe(false);


    component.touch('email');


    expect(
      component.shouldShowError('email')
    )
      .toBe(true);

  });


  // =================================================
  // SHOULD SHOW VALID
  // =================================================

  it('should show valid state only after field is touched', () => {

    component.email =
      'mario@email.it';


    expect(
      component.shouldShowValid('email')
    )
      .toBe(false);


    component.touch('email');


    expect(
      component.shouldShowValid('email')
    )
      .toBe(true);

  });


  // =================================================
  // TOGGLE PASSWORD
  // =================================================

  it('should toggle password visibility', () => {

    expect(
      component.showPassword
    )
      .toBe(false);


    component.togglePassword();


    expect(
      component.showPassword
    )
      .toBe(true);


    component.togglePassword();


    expect(
      component.showPassword
    )
      .toBe(false);

  });


  // =================================================
  // TOGGLE CONFERMA PASSWORD
  // =================================================

  it('should toggle confirm password visibility', () => {

    expect(
      component.showConfirmPassword
    )
      .toBe(false);


    component.toggleConfirmPassword();


    expect(
      component.showConfirmPassword
    )
      .toBe(true);


    component.toggleConfirmPassword();


    expect(
      component.showConfirmPassword
    )
      .toBe(false);

  });


  // =================================================
  // FORM COMPLETO VALIDO
  // =================================================

  it('should validate complete form correctly', () => {

    component.nome =
      'Mario';

    component.cognome =
      'Rossi';

    component.email =
      'mario@email.it';

    component.telefono =
      '3331234567';

    component.password =
      'Password1';

    component.confirmPassword =
      'Password1';


    expect(
      component.isFormValid()
    )
      .toBe(true);

  });


  // =================================================
  // FORM COMPLETO NON VALIDO
  // =================================================

  it('should reject invalid complete form', () => {

    component.nome =
      '';

    component.cognome =
      '';

    component.email =
      'email';

    component.telefono =
      '123';

    component.password =
      'password';

    component.confirmPassword =
      'password';


    expect(
      component.isFormValid()
    )
      .toBe(false);

  });


  // =================================================
  // REGISTER - FORM NON VALIDO
  // =================================================

  it('should not register when form is invalid', () => {

    component.nome =
      '';

    component.cognome =
      '';

    component.email =
      'email';

    component.telefono =
      '';

    component.password =
      'password';

    component.confirmPassword =
      'password';


    component.register();


    expect(
      component.errorMessage
    )
      .toBe(
        'Controlla i dati inseriti nel modulo.'
      );


    expect(
      component.loading
    )
      .toBe(false);


    expect(
      authServiceMock.register
    )
      .not.toHaveBeenCalled();


    expect(
      component.touched.nome
    )
      .toBe(true);


    expect(
      component.touched.email
    )
      .toBe(true);


    expect(
      component.touched.password
    )
      .toBe(true);

  });


  // =================================================
  // REGISTER - SUCCESSO
  // =================================================

  it('should register successfully', () => {

    component.nome =
      'Mario';

    component.cognome =
      'Rossi';

    component.email =
      'mario@email.it';

    component.telefono =
      '3331234567';

    component.password =
      'Password1';

    component.confirmPassword =
      'Password1';


    authServiceMock.register
      .mockReturnValue(
        of(mockUser)
      );


    component.register();


    expect(
      authServiceMock.register
    )
      .toHaveBeenCalledWith({

        nome: 'Mario',

        cognome: 'Rossi',

        email: 'mario@email.it',

        telefono: '3331234567',

        password: 'Password1'

      });


    expect(
      component.loading
    )
      .toBe(false);


    expect(
      component.showRegistrationSuccess()
    )
      .toBe(true);


    expect(component.nome)
      .toBe('');


    expect(component.cognome)
      .toBe('');


    expect(component.email)
      .toBe('');


    expect(component.telefono)
      .toBe('');


    expect(component.password)
      .toBe('');


    expect(component.confirmPassword)
      .toBe('');


    expect(
      component.touched.nome
    )
      .toBe(false);


    expect(
      component.touched.password
    )
      .toBe(false);


    expect(
      component.showPassword
    )
      .toBe(false);


    expect(
      component.showConfirmPassword
    )
      .toBe(false);

  });


  // =================================================
  // REGISTER - ACCOUNT GIÀ ESISTENTE
  // =================================================

  it('should show already registered popup for status 409', () => {

    component.nome =
      'Mario';

    component.cognome =
      'Rossi';

    component.email =
      'mario@email.it';

    component.telefono =
      '3331234567';

    component.password =
      'Password1';

    component.confirmPassword =
      'Password1';


    authServiceMock.register
      .mockReturnValue(

        throwError(() => ({
          status: 409
        }))

      );


    component.register();


    expect(
      component.loading
    )
      .toBe(false);


    expect(
      component.showAlreadyRegistered()
    )
      .toBe(true);

  });


  // =================================================
  // REGISTER - ERRORE 400
  // =================================================

  it('should show validation error for status 400', () => {

    component.nome =
      'Mario';

    component.cognome =
      'Rossi';

    component.email =
      'mario@email.it';

    component.telefono =
      '3331234567';

    component.password =
      'Password1';

    component.confirmPassword =
      'Password1';


    authServiceMock.register
      .mockReturnValue(

        throwError(() => ({
          status: 400
        }))

      );


    component.register();


    expect(
      component.loading
    )
      .toBe(false);


    expect(
      component.errorMessage
    )
      .toBe(
        'I dati inseriti non sono validi.'
      );

  });


  // =================================================
  // REGISTER - ERRORE GENERICO
  // =================================================

  it('should show generic error for unexpected status', () => {

    component.nome =
      'Mario';

    component.cognome =
      'Rossi';

    component.email =
      'mario@email.it';

    component.telefono =
      '3331234567';

    component.password =
      'Password1';

    component.confirmPassword =
      'Password1';


    authServiceMock.register
      .mockReturnValue(

        throwError(() => ({
          status: 500
        }))

      );


    component.register();


    expect(
      component.loading
    )
      .toBe(false);


    expect(
      component.errorMessage
    )
      .toBe(
        'Errore durante la registrazione. Riprova più tardi.'
      );

  });


  // =================================================
  // CLOSE ACCOUNT ESISTENTE
  // =================================================

  it('should close already registered popup and emit close', () => {

    const closeSpy =
      vi.spyOn(
        component.close,
        'emit'
      );


    component.showAlreadyRegistered
      .set(true);


    component.closeAlreadyRegistered();


    expect(
      component.showAlreadyRegistered()
    )
      .toBe(false);


    expect(closeSpy)
      .toHaveBeenCalled();

  });


  // =================================================
  // ACCOUNT ESISTENTE → LOGIN
  // =================================================

  it('should go to login from already registered popup', () => {

    const closeSpy =
      vi.spyOn(
        component.close,
        'emit'
      );

    const loginSpy =
      vi.spyOn(
        component.login,
        'emit'
      );


    component.showAlreadyRegistered
      .set(true);


    component.goToLogin();


    expect(
      component.showAlreadyRegistered()
    )
      .toBe(false);


    expect(closeSpy)
      .toHaveBeenCalled();


    expect(loginSpy)
      .toHaveBeenCalled();

  });


  // =================================================
  // CLOSE REGISTRATION SUCCESS
  // =================================================

  it('should close registration success popup', () => {

    const closeSpy =
      vi.spyOn(
        component.close,
        'emit'
      );


    component.showRegistrationSuccess
      .set(true);


    component.closeRegistrationSuccess();


    expect(
      component.showRegistrationSuccess()
    )
      .toBe(false);


    expect(closeSpy)
      .toHaveBeenCalled();

  });


  // =================================================
  // SUCCESS → LOGIN
  // =================================================

  it('should go to login after successful registration', () => {

    const closeSpy =
      vi.spyOn(
        component.close,
        'emit'
      );

    const loginSpy =
      vi.spyOn(
        component.login,
        'emit'
      );


    component.showRegistrationSuccess
      .set(true);


    component.goToLoginAfterRegistration();


    expect(
      component.showRegistrationSuccess()
    )
      .toBe(false);


    expect(closeSpy)
      .toHaveBeenCalled();


    expect(loginSpy)
      .toHaveBeenCalled();

  });


  // =================================================
  // CLOSE REGISTER
  // =================================================

  it('should emit close when not loading', () => {

    const closeSpy =
      vi.spyOn(
        component.close,
        'emit'
      );


    component.loading =
      false;


    component.closeRegister();


    expect(closeSpy)
      .toHaveBeenCalled();

  });


  // =================================================
  // NON CHIUDERE DURANTE LOADING
  // =================================================

  it('should not emit close while loading', () => {

    const closeSpy =
      vi.spyOn(
        component.close,
        'emit'
      );


    component.loading =
      true;


    component.closeRegister();


    expect(closeSpy)
      .not.toHaveBeenCalled();

  });

});