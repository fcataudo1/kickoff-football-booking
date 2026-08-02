package com.fpc.football_booking.controller;


import com.fpc.football_booking.dto.ReservationDto;
import com.fpc.football_booking.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/reservations")
public class ReservationController {


    private final ReservationService reservationService;


    public ReservationController(
            ReservationService reservationService
    ) {

        this.reservationService = reservationService;

    }



    @PostMapping
    public ResponseEntity<ReservationDto> create(
            @RequestBody ReservationDto dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        reservationService.createReservation(dto)
                );

    }




    @GetMapping
    public List<ReservationDto> getAll() {

        return reservationService.getAllReservations();

    }




    @GetMapping("/field/{fieldId}")
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
    public ResponseEntity<Void> cancel(
            @PathVariable Long id
    ) {

        reservationService.cancelReservation(id);

        return ResponseEntity
                .noContent()
                .build();

    }

}