package com.fpc.football_booking.entity;

import com.fpc.football_booking.entity.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
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
    @Builder.Default
    private ReservationStatus status = ReservationStatus.CONFIRMED;

    @Column(nullable = false, precision = 8, scale = 2)
    private BigDecimal price;



    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "football_field_id",
            nullable = false
    )
    private FootballField footballField;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "user_id",
            nullable = false
    )
    private User user;

}
