package com.fpc.football_booking.dto;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FootballFieldDto {

    private Long id;

    @NotBlank(message = "Football field name is required")
    private String name;

    private boolean active;
}