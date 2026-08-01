package com.fpc.football_booking.dto;

import com.fpc.football_booking.entity.enums.ReservationStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationDto {

    private Long id;

    private Long userId;

    private Long footballFieldId;

    private LocalDate reservationDate;

    private LocalTime startTime;

    private ReservationStatus status;
}