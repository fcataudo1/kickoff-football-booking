package com.fpc.football_booking.mapper;

import com.fpc.football_booking.dto.UserRequestDto;
import com.fpc.football_booking.dto.UserResponseDto;
import com.fpc.football_booking.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRequestDto dto) {

        return User.builder()
                .nome(dto.getNome())
                .cognome(dto.getCognome())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .telefono(dto.getTelefono())
                .build();
    }

    public UserResponseDto toResponseDto(User user) {

        return UserResponseDto.builder()
                .id(user.getId())
                .nome(user.getNome())
                .cognome(user.getCognome())
                .email(user.getEmail())
                .telefono(user.getTelefono())
                .ruolo(user.getRuolo())
                .build();
    }
}
