package com.fpc.football_booking.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDto {

    private String nome;

    private String cognome;

    private String email;

    private String password;

    private String telefono;
}