package com.fpc.football_booking.repository;


import com.fpc.football_booking.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Repository
public interface ReservationRepository
        extends JpaRepository<Reservation, Long> {


    @Query("""
        SELECT COUNT(r) > 0
        FROM Reservation r
        WHERE r.user.id = :userId
        AND r.reservationDate = :date
        AND r.startTime = :time
        AND r.status = 'CONFIRMED'
    """)
    boolean existsConfirmedReservation(
            @Param("userId") Long userId,
            @Param("date") LocalDate date,
            @Param("time") LocalTime time
    );


    List<Reservation> findByFootballFieldIdAndReservationDate(
            Long footballFieldId,
            LocalDate reservationDate
    );


    List<Reservation> findByUserId(
            Long userId
    );
}