package com.fpc.football_booking.dto;


import com.fpc.football_booking.entity.enums.ReservationStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "Customer name is required")
    private String customerName;

    @NotBlank(message = "Customer phone is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "Customer phone must contain 10 digits"
    )
    private String customerPhone;

    @Email(message = "Customer email must be valid")
    private String customerEmail;

    @NotNull(message = "Reservation date is required")
    private LocalDate reservationDate;

    @NotNull(message = "Start time is required")
    private LocalTime startTime;

    private ReservationStatus status;

    private BigDecimal price;

    private FootballFieldDto footballField;
}