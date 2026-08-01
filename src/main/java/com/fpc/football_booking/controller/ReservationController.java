package com.fpc.football_booking.controller;


import com.fpc.football_booking.dto.ReservationDto;
import com.fpc.football_booking.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {


    private final ReservationService reservationService;


    public ReservationController(
            ReservationService reservationService
    ){

        this.reservationService = reservationService;

    }


    @PostMapping
    public ReservationDto create(
            @RequestBody ReservationDto dto
    ){

        return reservationService.createReservation(dto);

    }


    @GetMapping("/user/{userId}")
    public List<ReservationDto> getByUser(
            @PathVariable Long userId
    ){

        return reservationService.getUserReservations(userId);

    }


    @GetMapping("/field/{fieldId}")
    public List<ReservationDto> getByField(
            @PathVariable Long fieldId,
            @RequestParam LocalDate date
    ){

        return reservationService.getFieldReservations(
                fieldId,
                date
        );

    }


    @DeleteMapping("/{id}")
    public void cancel(
            @PathVariable Long id
    ){

        reservationService.cancelReservation(id);

    }

}
