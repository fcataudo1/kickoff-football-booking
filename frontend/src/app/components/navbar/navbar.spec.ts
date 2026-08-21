import {
    ComponentFixture,
    TestBed
} from '@angular/core/testing';

import {
    BehaviorSubject
} from 'rxjs';

import {
    NavbarComponent
} from './navbar';

import {
    AuthService
} from '../../services/auth.service';

import {
    vi
} from 'vitest';


describe('NavbarComponent', () => {

    let component: NavbarComponent;

    let fixture: ComponentFixture<NavbarComponent>;

    let authServiceMock: {

        isLoggedIn: ReturnType<typeof vi.fn>;

        getUser: ReturnType<typeof vi.fn>;

        logout: ReturnType<typeof vi.fn>;

        loggedIn$: BehaviorSubject<boolean>;

    };


    // =================================================
    // MOCK UTENTE
    // =================================================

    const clientUser = {

        id: 1,

        nome: 'Mario',

        cognome: 'Rossi',

        email: 'mario@email.it',

        telefono: '3331234567',

        ruolo: 'CLIENTE'

    };


    const receptionistUser = {

        id: 2,

        nome: 'Luca',

        cognome: 'Bianchi',

        email: 'luca@email.it',

        telefono: '3337654321',

        ruolo: 'RECEPTIONIST'

    };


    const adminUser = {

        id: 3,

        nome: 'Admin',

        cognome: 'Test',

        email: 'admin@email.it',

        telefono: '3331111111',

        ruolo: 'ADMIN'

    };


    // =================================================
    // SETUP
    // =================================================

    beforeEach(async () => {

        authServiceMock = {

            isLoggedIn:
                vi.fn(),

            getUser:
                vi.fn(),

            logout:
                vi.fn(),

            loggedIn$:
                new BehaviorSubject<boolean>(false)

        };


        await TestBed.configureTestingModule({

            imports: [

                NavbarComponent

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
                NavbarComponent
            );


        component =
            fixture.componentInstance;

    });


    afterEach(() => {

        authServiceMock.loggedIn$.complete();

    });


    // =================================================
    // CREAZIONE COMPONENTE
    // =================================================

    it('should create', () => {

        expect(component)
            .toBeTruthy();

    });


    // =================================================
    // INIT UTENTE LOGGATO
    // =================================================

    it('should initialize authentication state', () => {

        authServiceMock.isLoggedIn
            .mockReturnValue(true);

        authServiceMock.getUser
            .mockReturnValue(clientUser);


        component.ngOnInit();


        expect(component.isLoggedIn)
            .toBe(true);


        expect(component.userName)
            .toBe('Mario');


        expect(component.userRole)
            .toBe('CLIENTE');

    });


    // =================================================
    // INIT UTENTE NON LOGGATO
    // =================================================

    it('should initialize as not logged in', () => {

        authServiceMock.isLoggedIn
            .mockReturnValue(false);

        authServiceMock.getUser
            .mockReturnValue(null);


        component.ngOnInit();


        expect(component.isLoggedIn)
            .toBe(false);


        expect(component.userName)
            .toBe('');


        expect(component.userRole)
            .toBe('');

    });


    // =================================================
    // USERNAME
    // =================================================

    it('should return user first name', () => {

        authServiceMock.getUser
            .mockReturnValue(clientUser);


        expect(component.userName)
            .toBe('Mario');

    });


    // =================================================
    // USERNAME SENZA UTENTE
    // =================================================

    it('should return empty userName when no user exists', () => {

        authServiceMock.getUser
            .mockReturnValue(null);


        expect(component.userName)
            .toBe('');

    });


    // =================================================
    // RUOLO
    // =================================================

    it('should return user role', () => {

        authServiceMock.getUser
            .mockReturnValue(receptionistUser);


        expect(component.userRole)
            .toBe('RECEPTIONIST');

    });


    // =================================================
    // RUOLO SENZA UTENTE
    // =================================================

    it('should return empty role when no user exists', () => {

        authServiceMock.getUser
            .mockReturnValue(null);


        expect(component.userRole)
            .toBe('');

    });


    // =================================================
    // CLIENTE
    // =================================================

    it('should identify CLIENTE role', () => {

        authServiceMock.getUser
            .mockReturnValue(clientUser);


        expect(component.isCliente)
            .toBe(true);


        expect(component.isReceptionist)
            .toBe(false);


        expect(component.isAdmin)
            .toBe(false);

    });


    // =================================================
    // RECEPTIONIST
    // =================================================

    it('should identify RECEPTIONIST role', () => {

        authServiceMock.getUser
            .mockReturnValue(receptionistUser);


        expect(component.isCliente)
            .toBe(false);


        expect(component.isReceptionist)
            .toBe(true);


        expect(component.isAdmin)
            .toBe(false);

    });


    // =================================================
    // ADMIN
    // =================================================

    it('should identify ADMIN role', () => {

        authServiceMock.getUser
            .mockReturnValue(adminUser);


        expect(component.isCliente)
            .toBe(false);


        expect(component.isReceptionist)
            .toBe(false);


        expect(component.isAdmin)
            .toBe(true);

    });


    // =================================================
    // ROLE LABEL CLIENTE
    // =================================================

    it('should return Cliente role label', () => {

        authServiceMock.getUser
            .mockReturnValue(clientUser);


        expect(component.roleLabel)
            .toBe('Cliente');

    });


    // =================================================
    // ROLE LABEL RECEPTIONIST
    // =================================================

    it('should return Receptionist role label', () => {

        authServiceMock.getUser
            .mockReturnValue(receptionistUser);


        expect(component.roleLabel)
            .toBe('Receptionist');

    });


    // =================================================
    // ROLE LABEL ADMIN
    // =================================================

    it('should return Amministratore role label', () => {

        authServiceMock.getUser
            .mockReturnValue(adminUser);


        expect(component.roleLabel)
            .toBe('Amministratore');

    });


    // =================================================
    // ROLE LABEL DEFAULT
    // =================================================

    it('should return Utente role label for unknown role', () => {

        authServiceMock.getUser
            .mockReturnValue({

                ...clientUser,

                ruolo: 'UNKNOWN'

            });


        expect(component.roleLabel)
            .toBe('Utente');

    });


    // =================================================
    // LOGIN EVENT
    // =================================================

    it('should emit login event', () => {

        const emitSpy =
            vi.spyOn(
                component.loginClick,
                'emit'
            );


        component.openLogin();


        expect(emitSpy)
            .toHaveBeenCalled();

    });


    // =================================================
    // REGISTER EVENT
    // =================================================

    it('should emit register event', () => {

        const emitSpy =
            vi.spyOn(
                component.registerClick,
                'emit'
            );


        component.openRegister();


        expect(emitSpy)
            .toHaveBeenCalled();

    });


    // =================================================
    // PROFILE EVENT
    // =================================================

    it('should emit profile event', () => {

        const emitSpy =
            vi.spyOn(
                component.profileClick,
                'emit'
            );


        component.openProfile();


        expect(emitSpy)
            .toHaveBeenCalled();

    });


    // =================================================
    // MY RESERVATIONS EVENT
    // =================================================

    it('should emit my reservations event', () => {

        const emitSpy =
            vi.spyOn(
                component.myReservationsClick,
                'emit'
            );


        component.openMyReservations();


        expect(emitSpy)
            .toHaveBeenCalled();

    });


    // =================================================
    // RECEPTIONIST RESERVATIONS EVENT
    // =================================================

    it('should emit receptionist reservations event', () => {

        const emitSpy =
            vi.spyOn(
                component.receptionistReservationsClick,
                'emit'
            );


        component.openReceptionistReservations();


        expect(emitSpy)
            .toHaveBeenCalled();

    });


    // =================================================
    // ADMIN USERS EVENT
    // =================================================

    it('should emit admin users event', () => {

        const emitSpy =
            vi.spyOn(
                component.adminUsersClick,
                'emit'
            );


        component.openAdminUsers();


        expect(emitSpy)
            .toHaveBeenCalled();

    });


    // =================================================
    // APERTURA POPUP LOGOUT
    // =================================================

    it('should open logout confirmation', () => {

        component.showLogoutConfirm =
            false;


        component.openLogoutConfirm();


        expect(component.showLogoutConfirm)
            .toBe(true);

    });


    // =================================================
    // ANNULLA LOGOUT
    // =================================================

    it('should cancel logout confirmation', () => {

        component.showLogoutConfirm =
            true;


        component.cancelLogout();


        expect(component.showLogoutConfirm)
            .toBe(false);

    });


    // =================================================
    // CONFERMA LOGOUT
    // =================================================

    it('should logout successfully', () => {

        authServiceMock.isLoggedIn
            .mockReturnValue(true);


        component.isLoggedIn =
            true;

        component.showLogoutConfirm =
            true;


        component.confirmLogout();


        expect(authServiceMock.logout)
            .toHaveBeenCalled();


        expect(component.showLogoutConfirm)
            .toBe(false);

    });


    // =================================================
    // LOGOUT AGGIORNA STATO
    // =================================================

    it('should update authentication state after logout', () => {

        authServiceMock.isLoggedIn
            .mockReturnValue(true);


        component.isLoggedIn =
            true;


        authServiceMock.logout
            .mockImplementation(() => {

                authServiceMock.isLoggedIn
                    .mockReturnValue(false);

            });


        component.confirmLogout();


        expect(authServiceMock.logout)
            .toHaveBeenCalled();


        expect(component.isLoggedIn)
            .toBe(false);

    });


    // =================================================
    // CAMBIO STATO AUTENTICAZIONE
    // =================================================

    it('should update authentication state when loggedIn$ emits', () => {

        authServiceMock.isLoggedIn
            .mockReturnValue(false);


        component.ngOnInit();


        expect(component.isLoggedIn)
            .toBe(false);


        authServiceMock.isLoggedIn
            .mockReturnValue(true);


        authServiceMock.loggedIn$.next(true);


        expect(component.isLoggedIn)
            .toBe(true);

    });


    // =================================================
    // DESTROY
    // =================================================

    it('should unsubscribe when component is destroyed', () => {

        authServiceMock.isLoggedIn
            .mockReturnValue(false);


        component.ngOnInit();


        const subscription =
            component['authSubscription'];


        expect(subscription)
            .toBeTruthy();


        component.ngOnDestroy();


        expect(subscription?.closed)
            .toBe(true);

    });


    // =================================================
    // LOGOUT NON MODIFICA DIRETTAMENTE L'EVENTO
    // =================================================

    it('should not emit navigation events when logging out', () => {

        const loginSpy =
            vi.spyOn(
                component.loginClick,
                'emit'
            );

        const registerSpy =
            vi.spyOn(
                component.registerClick,
                'emit'
            );

        const profileSpy =
            vi.spyOn(
                component.profileClick,
                'emit'
            );


        authServiceMock.isLoggedIn
            .mockReturnValue(false);


        component.confirmLogout();


        expect(authServiceMock.logout)
            .toHaveBeenCalled();


        expect(loginSpy)
            .not.toHaveBeenCalled();


        expect(registerSpy)
            .not.toHaveBeenCalled();


        expect(profileSpy)
            .not.toHaveBeenCalled();

    });

});