import {
    Component,
    inject,
    OnInit,
    ChangeDetectorRef,
    output
} from '@angular/core';

import { CommonModule } from '@angular/common';

import { HttpClient } from '@angular/common/http';

import { User } from '../../models/user';


@Component({
    selector: 'app-admin-users',

    standalone: true,

    imports: [
        CommonModule
    ],

    templateUrl: './admin-users.html',

    styleUrl: './admin-users.css'
})
export class AdminUsersComponent
    implements OnInit {


    // =========================
    // HTTP
    // =========================

    private readonly http =
        inject(HttpClient);


    // =========================
    // CHANGE DETECTOR
    // =========================

    private readonly changeDetector =
        inject(ChangeDetectorRef);


    // =========================
    // EVENTO CHIUSURA
    // =========================

    close =
        output<void>();


    // =========================
    // API
    // =========================

    private readonly apiUrl =
        'http://localhost:8080/api/users';


    // =========================
    // STATO
    // =========================

    users: User[] = [];

    loading = false;

    errorMessage = '';


    // =========================
    // POPUP DETTAGLI
    // =========================

    showDetailsModal = false;

    selectedUser: User | null = null;


    // =========================
    // INIT
    // =========================

    ngOnInit(): void {

        this.loadUsers();

    }


    // =========================
    // CARICA UTENTI
    // =========================

    loadUsers(): void {

        this.loading = true;

        this.errorMessage = '';


        this.http
            .get<User[]>(
                this.apiUrl
            )
            .subscribe({

                next: users => {

                    console.log(
                        'UTENTI ADMIN:',
                        users
                    );


                    this.users =
                        users;

                    this.loading =
                        false;


                    this.changeDetector.detectChanges();

                },


                error: error => {

                    console.error(
                        'Errore caricamento utenti:',
                        error
                    );


                    this.loading =
                        false;

                    this.errorMessage =
                        'Non è stato possibile caricare gli utenti.';


                    this.changeDetector.detectChanges();

                }

            });

    }


    // =========================
    // CONTA UTENTI PER RUOLO
    // =========================

    getUsersByRole(
        role: 'CLIENTE' | 'RECEPTIONIST' | 'ADMIN'
    ): number {

        return this.users.filter(
            user => user.ruolo === role
        ).length;

    }


    // =========================
    // APRI DETTAGLI
    // =========================

    openDetails(
        user: User
    ): void {

        this.selectedUser =
            user;

        this.showDetailsModal =
            true;


        this.changeDetector.detectChanges();

    }


    // =========================
    // CHIUDI DETTAGLI
    // =========================

    closeDetails(): void {

        this.showDetailsModal =
            false;

        this.selectedUser =
            null;

    }


    // =========================
    // ETICHETTA RUOLO
    // =========================

    getRoleLabel(
        role: User['ruolo']
    ): string {

        switch (role) {

            case 'CLIENTE':
                return 'Cliente';

            case 'RECEPTIONIST':
                return 'Receptionist';

            case 'ADMIN':
                return 'Amministratore';

            default:
                return role;

        }

    }


    // =========================
    // CLASSE RUOLO
    // =========================

    getRoleClass(
        role: User['ruolo']
    ): string {

        switch (role) {

            case 'CLIENTE':
                return 'cliente';

            case 'RECEPTIONIST':
                return 'receptionist';

            case 'ADMIN':
                return 'admin';

            default:
                return '';

        }

    }


    // =========================
    // CHIUDI FINESTRA
    // =========================

    closeWindow(): void {

        if (
            this.loading
        ) {

            return;

        }


        if (
            this.showDetailsModal
        ) {

            return;

        }


        this.close.emit();

    }


    // =========================
    // TRACK BY
    // =========================

    trackByUserId(
        index: number,
        user: User
    ): number {

        return user.id;

    }

}