package com.fpc.football_booking.entity.dto;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FootballFieldDto {

    private Long id;

    private String name;

    private BigDecimal pricePerHour;

    private boolean active;
}
