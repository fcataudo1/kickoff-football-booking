import {
    ComponentFixture,
    TestBed
} from '@angular/core/testing';

import {
    describe,
    it,
    expect,
    beforeEach,
    afterEach,
    vi
} from 'vitest';

import { of, throwError } from 'rxjs';

import { LoginComponent } from './login';

import { AuthService } from '../../services/auth.service';


describe('LoginComponent', () => {

    let component: LoginComponent;

    let fixture: ComponentFixture<LoginComponent>;

    let authServiceMock: {
        login: ReturnType<typeof vi.fn>;
    };


    // =================================================
    // SETUP
    // =================================================

    beforeEach(async () => {

        authServiceMock = {

            login: vi.fn()

        };


        await TestBed.configureTestingModule({

            imports: [
                LoginComponent
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
                LoginComponent
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
    // VALIDAZIONE EMAIL
    // =================================================

    it('should validate a correct email', () => {

        component.email =
            'mario.rossi@email.it';


        expect(
            component.isEmailValid()
        ).toBe(true);

    });


    it('should reject an invalid email', () => {

        component.email =
            'email-non-valida';


        expect(
            component.isEmailValid()
        ).toBe(false);

    });


    it('should reject an empty email', () => {

        component.email =
            '';


        expect(
            component.isEmailValid()
        ).toBe(false);

    });


    it('should trim email before validation', () => {

        component.email =
            '   mario@email.it   ';


        expect(
            component.isEmailValid()
        ).toBe(true);

    });


    // =================================================
    // VALIDAZIONE PASSWORD
    // =================================================

    it('should validate a non-empty password', () => {

        component.password =
            'password123';


        expect(
            component.isPasswordValid()
        ).toBe(true);

    });


    it('should reject an empty password', () => {

        component.password =
            '';


        expect(
            component.isPasswordValid()
        ).toBe(false);

    });


    it('should reject a password containing only spaces', () => {

        component.password =
            '     ';


        expect(
            component.isPasswordValid()
        ).toBe(false);

    });


    // =================================================
    // TOUCH
    // =================================================

    it('should mark email as touched', () => {

        component.touch('email');


        expect(
            component.touched.email
        ).toBe(true);

    });


    it('should mark password as touched', () => {

        component.touch('password');


        expect(
            component.touched.password
        ).toBe(true);

    });


    // =================================================
    // SHOULD SHOW ERROR
    // =================================================

    it('should not show error before field is touched', () => {

        component.email =
            'email-non-valida';


        expect(
            component.shouldShowError('email')
        ).toBe(false);

    });


    it('should show email error after touching an invalid email', () => {

        component.email =
            'email-non-valida';


        component.touch('email');


        expect(
            component.shouldShowError('email')
        ).toBe(true);

    });


    it('should show password error after touching an invalid password', () => {

        component.password =
            '';


        component.touch('password');


        expect(
            component.shouldShowError('password')
        ).toBe(true);

    });


    // =================================================
    // SHOULD SHOW VALID
    // =================================================

    it('should show email as valid after touching a valid email', () => {

        component.email =
            'mario@email.it';


        component.touch('email');


        expect(
            component.shouldShowValid('email')
        ).toBe(true);

    });


    it('should show password as valid after touching a valid password', () => {

        component.password =
            'password123';


        component.touch('password');


        expect(
            component.shouldShowValid('password')
        ).toBe(true);

    });


    it('should not show valid state before field is touched', () => {

        component.email =
            'mario@email.it';


        expect(
            component.shouldShowValid('email')
        ).toBe(false);

    });


    // =================================================
    // TOGGLE PASSWORD
    // =================================================

    it('should toggle password visibility', () => {

        expect(
            component.showPassword
        ).toBe(false);


        component.togglePassword();


        expect(
            component.showPassword
        ).toBe(true);


        component.togglePassword();


        expect(
            component.showPassword
        ).toBe(false);

    });


    // =================================================
    // VALIDITÀ FORM
    // =================================================

    it('should consider form valid when email and password are valid', () => {

        component.email =
            'mario@email.it';

        component.password =
            'password123';


        expect(
            component.isFormValid()
        ).toBe(true);

    });


    it('should consider form invalid when email is invalid', () => {

        component.email =
            'email-non-valida';

        component.password =
            'password123';


        expect(
            component.isFormValid()
        ).toBe(false);

    });


    it('should consider form invalid when password is empty', () => {

        component.email =
            'mario@email.it';

        component.password =
            '';


        expect(
            component.isFormValid()
        ).toBe(false);

    });


    // =================================================
    // LOGIN - DATI NON VALIDI
    // =================================================

    it('should reject login when form is invalid', () => {

        component.email =
            'email-non-valida';

        component.password =
            '';


        component.login();


        expect(
            component.errorMessage
        ).toBe(
            'Controlla i dati inseriti nel modulo.'
        );


        expect(
            component.touched.email
        ).toBe(true);


        expect(
            component.touched.password
        ).toBe(true);


        expect(
            authServiceMock.login
        ).not.toHaveBeenCalled();

    });


    // =================================================
    // LOGIN - SUCCESSO
    // =================================================

    it('should login successfully', () => {

        const mockUser = {

            id: 1,

            nome: 'Mario',

            cognome: 'Rossi',

            email: 'mario@email.it',

            telefono: '3331234567',

            ruolo: 'CLIENTE'

        };


        component.email =
            '   mario@email.it   ';

        component.password =
            'password123';


        authServiceMock.login
            .mockReturnValue(
                of(mockUser)
            );


        const closeSpy =
            vi.spyOn(
                component.close,
                'emit'
            );


        component.login();


        expect(
            authServiceMock.login
        ).toHaveBeenCalledWith({

            email:
                'mario@email.it',

            password:
                'password123'

        });


        expect(
            component.loading
        ).toBe(false);


        expect(
            component.errorMessage
        ).toBe('');


        expect(
            closeSpy
        ).toHaveBeenCalled();

    });


    // =================================================
    // LOGIN - 400
    // =================================================

    it('should show invalid credentials message for status 400', () => {

        component.email =
            'mario@email.it';

        component.password =
            'password123';


        authServiceMock.login
            .mockReturnValue(

                throwError(() => ({

                    status: 400

                }))

            );


        component.login();


        expect(
            component.loading
        ).toBe(false);


        expect(
            component.errorMessage
        ).toBe(
            'Email o password non valide.'
        );

    });


    // =================================================
    // LOGIN - 401
    // =================================================

    it('should show invalid credentials message for status 401', () => {

        component.email =
            'mario@email.it';

        component.password =
            'password123';


        authServiceMock.login
            .mockReturnValue(

                throwError(() => ({

                    status: 401

                }))

            );


        component.login();


        expect(
            component.loading
        ).toBe(false);


        expect(
            component.errorMessage
        ).toBe(
            'Email o password non valide.'
        );

    });


    // =================================================
    // LOGIN - ERRORE GENERICO
    // =================================================

    it('should show generic error message for unexpected error', () => {

        component.email =
            'mario@email.it';

        component.password =
            'password123';


        authServiceMock.login
            .mockReturnValue(

                throwError(() => ({

                    status: 500

                }))

            );


        component.login();


        expect(
            component.loading
        ).toBe(false);


        expect(
            component.errorMessage
        ).toBe(
            'Errore durante il login. Riprova più tardi.'
        );

    });


    // =================================================
    // CHIUSURA LOGIN
    // =================================================

    it('should emit close when closing login and not loading', () => {

        const closeSpy =
            vi.spyOn(
                component.close,
                'emit'
            );


        component.loading =
            false;


        component.closeLogin();


        expect(
            closeSpy
        ).toHaveBeenCalled();

    });


    it('should not emit close while loading', () => {

        const closeSpy =
            vi.spyOn(
                component.close,
                'emit'
            );


        component.loading =
            true;


        component.closeLogin();


        expect(
            closeSpy
        ).not.toHaveBeenCalled();

    });


    // =================================================
    // OVERLAY CLICK
    // =================================================

    it('should close when clicking directly on the overlay', () => {

        const closeSpy =
            vi.spyOn(
                component.close,
                'emit'
            );


        const event = {

            target: 'overlay',

            currentTarget: 'overlay'

        } as unknown as MouseEvent;


        component.loading =
            false;


        component.onOverlayClick(event);


        expect(
            closeSpy
        ).toHaveBeenCalled();

    });


    it('should not close when clicking inside the modal', () => {

        const closeSpy =
            vi.spyOn(
                component.close,
                'emit'
            );


        const event = {

            target: 'inside',

            currentTarget: 'overlay'

        } as unknown as MouseEvent;


        component.loading =
            false;


        component.onOverlayClick(event);


        expect(
            closeSpy
        ).not.toHaveBeenCalled();

    });


    it('should not close overlay while loading', () => {

        const closeSpy =
            vi.spyOn(
                component.close,
                'emit'
            );


        const event = {

            target: 'overlay',

            currentTarget: 'overlay'

        } as unknown as MouseEvent;


        component.loading =
            true;


        component.onOverlayClick(event);


        expect(
            closeSpy
        ).not.toHaveBeenCalled();

    });

});