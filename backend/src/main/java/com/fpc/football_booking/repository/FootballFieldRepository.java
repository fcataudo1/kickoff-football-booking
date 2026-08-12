package com.fpc.football_booking.repository;


import com.fpc.football_booking.entity.FootballField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FootballFieldRepository
        extends JpaRepository<FootballField, Long> {


    List<FootballField> findByActiveTrue();


    Optional<FootballField> findByName(String name);


    boolean existsByName(String name);

    @Query("""
        SELECT f
        FROM FootballField f
        WHERE f.active = true
        AND f.id NOT IN (
            SELECT r.footballField.id
            FROM Reservation r
            WHERE r.reservationDate = :date
            AND r.startTime = :time
            AND r.status = 'CONFIRMED'
        )
        ORDER BY f.id
    """)
    List<FootballField> findAvailableFields(
            @Param("date") LocalDate date,
            @Param("time") LocalTime time
    );
}
