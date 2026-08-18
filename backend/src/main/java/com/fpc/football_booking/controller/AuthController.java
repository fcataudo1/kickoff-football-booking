package com.fpc.football_booking.controller;

import com.fpc.football_booking.dto.LoginRequestDto;
import com.fpc.football_booking.dto.LoginResponseDto;
import com.fpc.football_booking.exception.BusinessException;
import com.fpc.football_booking.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fpc.football_booking.entity.User;
import org.springframework.security.core.Authentication;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(
        name = "Authentication",
        description = "API per l'autenticazione degli utenti"
)
public class AuthController {

    private final AuthService authService;


    public AuthController(
            AuthService authService
    ) {
        this.authService = authService;
    }


    @PostMapping("/login")
    @Operation(
            summary = "Effettua il login",
            description = "Verifica le credenziali dell'utente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Login effettuato correttamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dati non validi"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Email o password non valide"
            )
    })
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto dto
    ) {

        return ResponseEntity.ok(
                authService.login(dto)
        );
    }


    @GetMapping("/me")
    @Operation(
            summary = "Recupera l'utente autenticato",
            description = "Restituisce i dati dell'utente associato al token JWT."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Utente autenticato recuperato correttamente"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticazione richiesta"
            )
    })
    public ResponseEntity<LoginResponseDto> me(
            Authentication authentication
    ) {

        if (authentication == null ||
                !authentication.isAuthenticated()) {

            throw new BusinessException(
                    "Authentication required"
            );
        }

        User user =
                (User) authentication.getPrincipal();

        return ResponseEntity.ok(
                LoginResponseDto.builder()
                        .id(user.getId())
                        .nome(user.getNome())
                        .cognome(user.getCognome())
                        .email(user.getEmail())
                        .telefono(user.getTelefono())
                        .ruolo(user.getRuolo())
                        .build()
        );
    }
}