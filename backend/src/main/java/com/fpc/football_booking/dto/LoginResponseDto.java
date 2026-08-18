package com.fpc.football_booking.dto;

import com.fpc.football_booking.entity.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponseDto {

    private Long id;

    private String nome;

    private String cognome;

    private String email;

    private String telefono;

    private Role ruolo;

    private String token;
}