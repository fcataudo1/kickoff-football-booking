package com.fpc.football_booking.controller;

import com.fpc.football_booking.dto.FootballFieldDto;
import com.fpc.football_booking.service.FootballFieldService;
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
@RequestMapping("/api/fields")
@CrossOrigin(origins = "http://localhost:4200")
@Tag(
        name = "Football Fields",
        description = "API per la gestione dei campi da calcio"
)
public class FootballFieldController {

    private final FootballFieldService fieldService;


    public FootballFieldController(
            FootballFieldService fieldService
    ) {

        this.fieldService = fieldService;

    }


    @GetMapping
    @Operation(
            summary = "Recupera tutti i campi",
            description = "Restituisce la lista di tutti i campi da calcio."
    )
    @ApiResponse(
            responseCode = "200",
            description = "Campi recuperati correttamente"
    )
    public List<FootballFieldDto> getAll() {

        return fieldService.getAll();

    }


    @GetMapping("/{id}")
    @Operation(
            summary = "Recupera un campo",
            description = "Restituisce un campo da calcio tramite il suo ID."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Campo trovato correttamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Campo non trovato"
            )
    })
    public FootballFieldDto getById(
            @PathVariable Long id
    ) {

        return fieldService.read(id);

    }


    @PostMapping
    @Operation(
            summary = "Crea un nuovo campo",
            description = "Crea un nuovo campo da calcio utilizzando i dati forniti."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Campo creato correttamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dati del campo non validi"
            )
    })
    public ResponseEntity<FootballFieldDto> create(
            @Valid @RequestBody FootballFieldDto dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        fieldService.insert(dto)
                );

    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Modifica un campo",
            description = "Aggiorna i dati di un campo da calcio esistente."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Campo modificato correttamente"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dati del campo non validi"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Campo non trovato"
            )
    })
    public FootballFieldDto update(
            @PathVariable Long id,
            @Valid @RequestBody FootballFieldDto dto
    ) {

        dto.setId(id);

        return fieldService.update(dto);

    }


    @PutMapping("/{id}/disable")
    @Operation(
            summary = "Disabilita un campo",
            description = "Disabilita un campo da calcio impedendone l'utilizzo per nuove prenotazioni."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "Campo disabilitato correttamente"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Campo non trovato"
            )
    })
    public ResponseEntity<Void> disable(
            @PathVariable Long id
    ) {

        fieldService.disable(id);

        return ResponseEntity
                .noContent()
                .build();

    }

}