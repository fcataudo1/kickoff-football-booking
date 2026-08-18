package com.fpc.football_booking.controller;


import com.fpc.football_booking.dto.ReservationDto;
import com.fpc.football_booking.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.fpc.football_booking.entity.User;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/reservations")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(
        name = "Reservations",
        description = "API per la gestione delle prenotazioni dei campi da calcio"
)
public class ReservationController {

    private final ReservationService reservationService;


    public ReservationController(
            ReservationService reservationService
    ) {
        this.reservationService = reservationService;
    }


    @PostMapping
    @Operation(
            summary = "Crea una nuova prenotazione",
            description = "Crea una prenotazione per un campo da calcio verificando la disponibilità e i vincoli previsti."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Prenotazione creata correttamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dati della prenotazione non validi"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utente non trovato"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Campo non disponibile o prenotazione non consentita"
            )
    })
    public ResponseEntity<ReservationDto> create(
            @Valid @RequestBody ReservationDto dto,
            Authentication authentication
    ) {

        User user = (User) authentication.getPrincipal();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        reservationService.createReservation(
                                dto,
                                user.getId()
                        )
                );
    }


    @GetMapping
    @Operation(
            summary = "Recupera tutte le prenotazioni",
            description = "Restituisce la lista di tutte le prenotazioni presenti nel sistema."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Prenotazioni recuperate correttamente"
    )
    public List<ReservationDto> getAll() {

        return reservationService.getAllReservations();
    }

    @GetMapping("/my")
    @Operation(
            summary = "Recupera le proprie prenotazioni",
            description = "Restituisce le prenotazioni dell'utente autenticato."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Prenotazioni recuperate correttamente"
    )
    public List<ReservationDto> getMyReservations(
            Authentication authentication
    ) {

        User user = (User) authentication.getPrincipal();

        return reservationService.getMyReservations(
                user.getId()
        );
    }

    @GetMapping("/field/{fieldId}")
    @Operation(
            summary = "Recupera le prenotazioni di un campo",
            description = "Restituisce le prenotazioni di un determinato campo per una specifica data."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Prenotazioni recuperate correttamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Campo non trovato"
            )
    })
    public List<ReservationDto> getByField(
            @PathVariable Long fieldId,
            @RequestParam LocalDate date
    ) {

        return reservationService.getFieldReservations(
                fieldId,
                date
        );
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Cancella una prenotazione",
            description = "Cancella una prenotazione esistente tramite il suo ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Prenotazione cancellata correttamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Prenotazione non trovata"
            )
    })
    public ResponseEntity<Void> cancel(
            @PathVariable Long id,
            Authentication authentication
    ) {

        User user = (User) authentication.getPrincipal();

        reservationService.cancelReservation(
                id,
                user.getId()
        );

        return ResponseEntity
                .noContent()
                .build();
    }
}