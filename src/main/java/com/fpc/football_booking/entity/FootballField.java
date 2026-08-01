package com.fpc.football_booking.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "football_fields")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FootballField {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String name;


    @Column(nullable = false)
    private BigDecimal pricePerHour;


    @Column(nullable = false)
    private boolean active;

}
