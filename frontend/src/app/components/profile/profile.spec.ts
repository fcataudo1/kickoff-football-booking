import {
    ComponentFixture,
    TestBed
} from '@angular/core/testing';

import {
    ProfileComponent
} from './profile';

import {
    AuthService
} from '../../services/auth.service';

import {
    vi
} from 'vitest';


describe('ProfileComponent', () => {

    let component: ProfileComponent;

    let fixture: ComponentFixture<ProfileComponent>;

    let authServiceMock: {

        getUser: ReturnType<typeof vi.fn>;

    };


    // =================================================
    // MOCK UTENTE CLIENTE
    // =================================================

    const clientUser = {

        id: 1,

        nome: 'Mario',

        cognome: 'Rossi',

        email: 'mario@email.it',

        telefono: '3331234567',

        ruolo: 'CLIENTE'

    };


    // =================================================
    // SETUP
    // =================================================

    beforeEach(async () => {

        authServiceMock = {

            getUser:
                vi.fn()

        };


        await TestBed.configureTestingModule({

            imports: [

                ProfileComponent

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
                ProfileComponent
            );


        component =
            fixture.componentInstance;

    });


    // =================================================
    // CREAZIONE
    // =================================================

    it('should create', () => {

        expect(component)
            .toBeTruthy();

    });


    // =================================================
    // USER
    // =================================================

    it('should return the authenticated user', () => {

        authServiceMock.getUser
            .mockReturnValue(clientUser);


        expect(component.user)
            .toEqual(clientUser);

    });


    // =================================================
    // USER NON PRESENTE
    // =================================================

    it('should return null when no user is authenticated', () => {

        authServiceMock.getUser
            .mockReturnValue(null);


        expect(component.user)
            .toBeNull();

    });


    // =================================================
    // NOME COMPLETO
    // =================================================

    it('should return the full name of the user', () => {

        authServiceMock.getUser
            .mockReturnValue(clientUser);


        expect(component.fullName)
            .toBe('Mario Rossi');

    });


    // =================================================
    // NOME COMPLETO SENZA UTENTE
    // =================================================

    it('should return an empty string when no user exists', () => {

        authServiceMock.getUser
            .mockReturnValue(null);


        expect(component.fullName)
            .toBe('');

    });


    // =================================================
    // RUOLO CLIENTE
    // =================================================

    it('should return Cliente role label', () => {

        authServiceMock.getUser
            .mockReturnValue({

                ...clientUser,

                ruolo: 'CLIENTE'

            });


        expect(component.roleLabel)
            .toBe('Cliente');

    });


    // =================================================
    // RUOLO RECEPTIONIST
    // =================================================

    it('should return Receptionist role label', () => {

        authServiceMock.getUser
            .mockReturnValue({

                ...clientUser,

                ruolo: 'RECEPTIONIST'

            });


        expect(component.roleLabel)
            .toBe('Receptionist');

    });


    // =================================================
    // RUOLO ADMIN
    // =================================================

    it('should return Amministratore role label', () => {

        authServiceMock.getUser
            .mockReturnValue({

                ...clientUser,

                ruolo: 'ADMIN'

            });


        expect(component.roleLabel)
            .toBe('Amministratore');

    });


    // =================================================
    // RUOLO SCONOSCIUTO
    // =================================================

    it('should return Utente for an unknown role', () => {

        authServiceMock.getUser
            .mockReturnValue({

                ...clientUser,

                ruolo: 'UNKNOWN'

            });


        expect(component.roleLabel)
            .toBe('Utente');

    });


    // =================================================
    // RUOLO SENZA UTENTE
    // =================================================

    it('should return Utente when no user exists', () => {

        authServiceMock.getUser
            .mockReturnValue(null);


        expect(component.roleLabel)
            .toBe('Utente');

    });


    // =================================================
    // CHIUSURA
    // =================================================

    it('should emit close event', () => {

        const emitSpy =
            vi.spyOn(
                component.close,
                'emit'
            );


        component.closeWindow();


        expect(emitSpy)
            .toHaveBeenCalled();

    });

});