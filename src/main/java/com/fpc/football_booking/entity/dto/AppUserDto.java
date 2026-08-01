package com.fpc.football_booking.entity.dto;

import com.fpc.football_booking.entity.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppUserDto {

    private Long id;

    private String firstName;

    private String lastName;

    private String email;

    private Role role;
}
