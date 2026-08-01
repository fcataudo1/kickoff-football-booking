package com.fpc.football_booking.service;


import com.fpc.football_booking.dto.ReservationDto;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {

    ReservationDto createReservation(ReservationDto dto);

    List<ReservationDto> getUserReservations(Long userId);

    List<ReservationDto> getFieldReservations(
            Long fieldId,
            LocalDate date
    );

    List<ReservationDto> getAllReservations();

    void cancelReservation(Long reservationId);

}
