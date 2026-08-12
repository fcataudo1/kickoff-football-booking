# ⚽ KickOff - Football Booking System

Applicazione web full-stack per la gestione e prenotazione di campi da calcio.

Il progetto permette agli utenti di selezionare una data e un orario, verificare la disponibilità dei campi e completare una prenotazione tramite un'interfaccia web semplice, moderna e responsive.

## 📸 Anteprima

### Homepage

<!-- Inserire screenshot della homepage -->

### Prenotazione

<!-- Inserire screenshot della sezione/form di prenotazione -->

### Responsive

<!-- Inserire screenshot della versione responsive -->

## 🚀 Tecnologie utilizzate

### Backend

- Java
- Spring Boot
- Spring Data JPA
- Hibernate
- PostgreSQL
- REST API
- Swagger / OpenAPI

## 🗄️ Database

Il progetto utilizza **PostgreSQL** per la gestione dei dati relativi a:

- Campi da calcio
- Prenotazioni
- Stato delle prenotazioni
- Data e orario delle partite
- Dati del cliente associati alla prenotazione

L'accesso al database viene gestito tramite **Spring Data JPA** e **Hibernate**.

Le operazioni di persistenza vengono effettuate dal backend attraverso repository JPA.

### Frontend

- Angular
- TypeScript
- Bootstrap
- Bootstrap Icons
- HTML5
- CSS3

## 🛠️ Strumenti e supporto allo sviluppo

- IntelliJ IDEA
- Visual Studio Code
- Postman
- Git / GitHub
- GitHub Desktop
- ChatGPT come supporto allo sviluppo, debugging e revisione del codice

## ⚽ Funzionalità principali

- Selezione della data della partita
- Selezione dell'orario disponibile
- Verifica della disponibilità dei campi
- Prenotazione di una partita
- Validazione dei dati inseriti
- Controllo delle prenotazioni già effettuate
- Prevenzione di prenotazioni multiple dello stesso numero di telefono nella stessa fascia oraria
- Gestione dello stato della prenotazione:
  - `CONFIRMED`
  - `CANCELLED`
- Visualizzazione della conferma della prenotazione
- Gestione degli errori e delle prenotazioni non disponibili
- Interfaccia responsive per desktop, tablet e smartphone

## 🗂️ Struttura del progetto

```text
KickOff/
│
├── backend/
│   └── src/
│       └── main/
│           └── java/
│
├── frontend/
│   └── src/
│       ├── app/
│       └── assets/
│
└── README.md
```

## 🔧 Backend

Il backend è sviluppato con **Spring Boot** e utilizza **PostgreSQL** come database.

Le principali operazioni vengono esposte tramite API REST per:

- Creare una prenotazione
- Recuperare tutte le prenotazioni
- Recuperare le prenotazioni di un determinato campo
- Annullare una prenotazione
- Recuperare i campi disponibili

### Endpoint principali

| Metodo | Endpoint                            | Descrizione                          |
| ------ | ----------------------------------- | ------------------------------------ |
| POST   | `/api/reservations`                 | Crea una prenotazione                |
| GET    | `/api/reservations`                 | Recupera tutte le prenotazioni       |
| GET    | `/api/reservations/field/{fieldId}` | Recupera le prenotazioni di un campo |
| DELETE | `/api/reservations/{id}`            | Annulla una prenotazione             |
| GET    | `/api/fields`                       | Recupera i campi disponibili         |

## 🎨 Frontend

Il frontend è sviluppato con **Angular** e utilizza componenti standalone.

L'interfaccia comprende:

- Navbar
- Hero section
- Sezione prenotazione
- Form di prenotazione
- Sezione servizi
- Video promozionale
- Vantaggi di KickOff
- Statistiche
- Sezione contatti
- Footer

Il layout è responsive grazie a **Bootstrap**.

## 📅 Regole di prenotazione

Le prenotazioni rispettano alcune regole definite nel backend:

- Le prenotazioni sono disponibili dalle **16:00 alle 23:00**
- Gli orari devono essere selezionati su un'ora intera
- Ogni prenotazione ha una durata di **1 ora**
- Lo stesso numero di telefono non può effettuare più prenotazioni nella stessa fascia oraria
- Non è possibile prenotare una data passata
- Il campo deve essere disponibile per la data e l'orario selezionati

## 💰 Prezzo

Il prezzo della prenotazione viene impostato dal backend a:

**50 € per partita**

## ▶️ Avvio del progetto

### Backend

Configurare PostgreSQL e inserire le credenziali del database nel file di configurazione del backend.

Successivamente avviare l'applicazione Spring Boot.

Il backend sarà disponibile all'indirizzo:

```text
http://localhost:8080
```

### Frontend

Entrare nella cartella `frontend` e installare le dipendenze:

```bash
npm install
```

Avviare l'applicazione Angular:

```bash
ng serve
```

Il frontend sarà disponibile all'indirizzo:

```text
http://localhost:4200
```

## 📖 API Documentation

Le API REST sono documentate tramite **Swagger / OpenAPI**.

Durante l'esecuzione del backend è possibile utilizzare Swagger UI per visualizzare e testare gli endpoint disponibili.

## 👨‍💻 Autore

**Francesco Pio Cataudo**

Progetto realizzato come applicazione full-stack per la gestione delle prenotazioni di campi da calcio.

