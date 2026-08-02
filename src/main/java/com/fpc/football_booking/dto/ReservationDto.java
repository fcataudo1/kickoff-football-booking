package com.fpc.football_booking.dto;


import com.fpc.football_booking.entity.enums.ReservationStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDto {


    private Long id;


    private String customerName;

    private String customerPhone;

    private String customerEmail;


    private Long footballFieldId;


    private LocalDate reservationDate;


    private LocalTime startTime;


    private ReservationStatus status;

    private BigDecimal price;

}