package com.fpc.football_booking.repository;


import com.fpc.football_booking.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Repository
public interface ReservationRepository
        extends JpaRepository<Reservation, Long> {


    boolean existsByFootballFieldIdAndReservationDateAndStartTime(
            Long footballFieldId,
            LocalDate reservationDate,
            LocalTime startTime
    );


    List<Reservation> findByFootballFieldIdAndReservationDate(
            Long footballFieldId,
            LocalDate reservationDate
    );


    List<Reservation> findByReservationDate(
            LocalDate reservationDate
    );



}