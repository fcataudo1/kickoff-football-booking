import {
    ComponentFixture,
    TestBed
} from '@angular/core/testing';

import {
    provideHttpClient
} from '@angular/common/http';

import {
    HttpTestingController,
    provideHttpClientTesting
} from '@angular/common/http/testing';

import {
    describe,
    it,
    expect,
    beforeEach,
    afterEach,
    vi
} from 'vitest';

import {
    AdminUsersComponent
} from './admin-users';

import {
    User
} from '../../models/user';


describe('AdminUsersComponent', () => {

    let component: AdminUsersComponent;

    let fixture: ComponentFixture<AdminUsersComponent>;

    let httpTestingController: HttpTestingController;


    const apiUrl =
        'http://localhost:8080/api/users';


    const mockUsers: User[] = [

        {
            id: 1,
            nome: 'Mario',
            cognome: 'Rossi',
            email: 'mario.rossi@email.it',
            telefono: '3331234567',
            ruolo: 'CLIENTE'
        },

        {
            id: 2,
            nome: 'Luca',
            cognome: 'Bianchi',
            email: 'luca.bianchi@email.it',
            telefono: '3337654321',
            ruolo: 'RECEPTIONIST'
        },

        {
            id: 3,
            nome: 'Francesco',
            cognome: 'Verdi',
            email: 'francesco.verdi@email.it',
            telefono: '3331112233',
            ruolo: 'ADMIN'
        },

        {
            id: 4,
            nome: 'Anna',
            cognome: 'Neri',
            email: 'anna.neri@email.it',
            telefono: '3334445566',
            ruolo: 'CLIENTE'
        }

    ];


    // =================================================
    // SETUP
    // =================================================

    beforeEach(async () => {

        await TestBed.configureTestingModule({

            imports: [
                AdminUsersComponent
            ],

            providers: [

                provideHttpClient(),

                provideHttpClientTesting()

            ]

        }).compileComponents();


        fixture =
            TestBed.createComponent(
                AdminUsersComponent
            );

        component =
            fixture.componentInstance;


        httpTestingController =
            TestBed.inject(
                HttpTestingController
            );


        fixture.detectChanges();


        // ngOnInit() esegue automaticamente loadUsers()
        const request =
            httpTestingController.expectOne(
                apiUrl
            );

        expect(request.request.method)
            .toBe('GET');


        request.flush(
            mockUsers
        );


        fixture.detectChanges();

    });


    // =================================================
    // CLEANUP
    // =================================================

    afterEach(() => {

        httpTestingController.verify();

    });


    // =================================================
    // CREAZIONE COMPONENTE
    // =================================================

    it('deve creare il componente', () => {

        expect(component)
            .toBeTruthy();

    });


    // =================================================
    // CARICAMENTO UTENTI
    // =================================================

    it('deve caricare correttamente gli utenti dal backend', () => {

        expect(component.users)
            .toEqual(mockUsers);

        expect(component.users.length)
            .toBe(4);

        expect(component.loading)
            .toBe(false);

        expect(component.errorMessage)
            .toBe('');

    });


    // =================================================
    // HTTP GET
    // =================================================

    it('deve effettuare una richiesta GET agli utenti', () => {

        component.loadUsers();

        const request =
            httpTestingController.expectOne(
                apiUrl
            );

        expect(request.request.method)
            .toBe('GET');

        request.flush(mockUsers);

        expect(component.users)
            .toEqual(mockUsers);

        expect(component.loading)
            .toBe(false);

    });


    // =================================================
    // LOADING
    // =================================================

    it('deve impostare loading durante il caricamento', () => {

        component.loadUsers();

        expect(component.loading)
            .toBe(true);


        const request =
            httpTestingController.expectOne(
                apiUrl
            );


        request.flush(mockUsers);

        expect(component.loading)
            .toBe(false);

    });


    // =================================================
    // ERRORE HTTP
    // =================================================

    it('deve gestire un errore durante il caricamento utenti', () => {

        component.loadUsers();

        const request =
            httpTestingController.expectOne(
                apiUrl
            );


        request.flush(
            'Errore server',
            {
                status: 500,
                statusText: 'Internal Server Error'
            }
        );


        expect(component.loading)
            .toBe(false);


        expect(component.errorMessage)
            .toBe(
                'Non è stato possibile caricare gli utenti.'
            );

    });


    // =================================================
    // CONTEGGIO CLIENTI
    // =================================================

    it('deve contare correttamente i clienti', () => {

        expect(
            component.getUsersByRole('CLIENTE')
        ).toBe(2);

    });


    // =================================================
    // CONTEGGIO RECEPTIONIST
    // =================================================

    it('deve contare correttamente i receptionist', () => {

        expect(
            component.getUsersByRole('RECEPTIONIST')
        ).toBe(1);

    });


    // =================================================
    // CONTEGGIO ADMIN
    // =================================================

    it('deve contare correttamente gli admin', () => {

        expect(
            component.getUsersByRole('ADMIN')
        ).toBe(1);

    });


    // =================================================
    // CONTEGGIO NESSUN RUOLO
    // =================================================

    it('deve restituire zero se non ci sono utenti del ruolo richiesto', () => {

        component.users = [
            {
                id: 10,
                nome: 'Test',
                cognome: 'User',
                email: 'test@email.it',
                telefono: '3330000000',
                ruolo: 'CLIENTE'
            }
        ];


        expect(
            component.getUsersByRole('ADMIN')
        ).toBe(0);


        expect(
            component.getUsersByRole('RECEPTIONIST')
        ).toBe(0);

    });


    // =================================================
    // APERTURA DETTAGLI
    // =================================================

    it('deve aprire il popup dei dettagli utente', () => {

        const user =
            mockUsers[0];


        component.openDetails(user);


        expect(component.selectedUser)
            .toBe(user);


        expect(component.showDetailsModal)
            .toBe(true);

    });


    // =================================================
    // CHIUSURA DETTAGLI
    // =================================================

    it('deve chiudere il popup dei dettagli', () => {

        component.selectedUser =
            mockUsers[0];

        component.showDetailsModal =
            true;


        component.closeDetails();


        expect(component.showDetailsModal)
            .toBe(false);


        expect(component.selectedUser)
            .toBeNull();

    });


    // =================================================
    // ETICHETTA CLIENTE
    // =================================================

    it('deve restituire l\'etichetta corretta per CLIENTE', () => {

        expect(
            component.getRoleLabel('CLIENTE')
        ).toBe('Cliente');

    });


    // =================================================
    // ETICHETTA RECEPTIONIST
    // =================================================

    it('deve restituire l\'etichetta corretta per RECEPTIONIST', () => {

        expect(
            component.getRoleLabel('RECEPTIONIST')
        ).toBe('Receptionist');

    });


    // =================================================
    // ETICHETTA ADMIN
    // =================================================

    it('deve restituire l\'etichetta corretta per ADMIN', () => {

        expect(
            component.getRoleLabel('ADMIN')
        ).toBe('Amministratore');

    });


    // =================================================
    // CLASSE CLIENTE
    // =================================================

    it('deve restituire la classe cliente', () => {

        expect(
            component.getRoleClass('CLIENTE')
        ).toBe('cliente');

    });


    // =================================================
    // CLASSE RECEPTIONIST
    // =================================================

    it('deve restituire la classe receptionist', () => {

        expect(
            component.getRoleClass('RECEPTIONIST')
        ).toBe('receptionist');

    });


    // =================================================
    // CLASSE ADMIN
    // =================================================

    it('deve restituire la classe admin', () => {

        expect(
            component.getRoleClass('ADMIN')
        ).toBe('admin');

    });


    // =================================================
    // CLASSE RUOLO NON RICONOSCIUTO
    // =================================================

    it('deve restituire una stringa vuota per un ruolo non riconosciuto', () => {

        expect(
            component.getRoleClass(
                'RUOLO_NON_VALIDO' as User['ruolo']
            )
        ).toBe('');

    });


    // =================================================
    // CHIUSURA FINESTRA
    // =================================================

    it('deve emettere l\'evento close quando viene chiusa la finestra', () => {

        const closeSpy =
            vi.spyOn(
                component.close,
                'emit'
            );


        component.loading =
            false;

        component.showDetailsModal =
            false;


        component.closeWindow();


        expect(closeSpy)
            .toHaveBeenCalled();

    });


    // =================================================
    // NON CHIUDE DURANTE LOADING
    // =================================================

    it('non deve chiudere la finestra durante il caricamento', () => {

        const closeSpy =
            vi.spyOn(
                component.close,
                'emit'
            );


        component.loading =
            true;


        component.closeWindow();


        expect(closeSpy)
            .not
            .toHaveBeenCalled();

    });


    // =================================================
    // NON CHIUDE SE DETTAGLI APERTI
    // =================================================

    it('non deve chiudere la finestra se il popup dettagli è aperto', () => {

        const closeSpy =
            vi.spyOn(
                component.close,
                'emit'
            );


        component.loading =
            false;

        component.showDetailsModal =
            true;


        component.closeWindow();


        expect(closeSpy)
            .not
            .toHaveBeenCalled();

    });


    // =================================================
    // TRACK BY
    // =================================================

    it('deve restituire l\'id dell\'utente nel trackBy', () => {

        const user =
            mockUsers[0];


        expect(
            component.trackByUserId(
                0,
                user
            )
        ).toBe(user.id);

    });

});