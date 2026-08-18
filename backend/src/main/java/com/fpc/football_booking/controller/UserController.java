package com.fpc.football_booking.controller;

import com.fpc.football_booking.dto.UserRequestDto;
import com.fpc.football_booking.dto.UserResponseDto;
import com.fpc.football_booking.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(
        name = "Users",
        description = "API per la gestione degli utenti"
)
public class UserController {

    private final UserService userService;


    public UserController(
            UserService userService
    ) {
        this.userService = userService;
    }


    @PostMapping
    @Operation(
            summary = "Registra un nuovo utente",
            description = "Crea un nuovo utente assegnandogli automaticamente il ruolo CLIENTE."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Utente registrato correttamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dati dell'utente non validi"
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email o numero di telefono già registrati"
            )
    })
    public ResponseEntity<UserResponseDto> create(
            @Valid @RequestBody UserRequestDto dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        userService.createUser(dto)
                );
    }


    @GetMapping
    @Operation(
            summary = "Recupera tutti gli utenti",
            description = "Restituisce la lista degli utenti presenti nel sistema."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Utenti recuperati correttamente"
    )
    public List<UserResponseDto> getAll() {

        return userService.getAllUsers();
    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Recupera un utente",
            description = "Restituisce un utente tramite il suo ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Utente recuperato correttamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utente non trovato"
            )
    })
    public UserResponseDto getById(
            @PathVariable Long id
    ) {

        return userService.getUserById(id);
    }


    @DeleteMapping("/{id}")
    @Operation(
            summary = "Elimina un utente",
            description = "Elimina un utente tramite il suo ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Utente eliminato correttamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Utente non trovato"
            )
    })
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        userService.deleteUser(id);

        return ResponseEntity
                .noContent()
                .build();
    }
}
