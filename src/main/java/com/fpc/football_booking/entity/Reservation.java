package com.fpc.football_booking.entity;

import com.fpc.football_booking.entity.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "reservations")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate reservationDate;


    @Column(nullable = false)
    private LocalTime startTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "football_field_id", nullable = false)
    private FootballField footballField;
}
