package com.fpc.football_booking.service;

import com.fpc.football_booking.dto.UserRequestDto;
import com.fpc.football_booking.dto.UserResponseDto;

import java.util.List;

public interface UserService {

    UserResponseDto createUser(
            UserRequestDto dto
    );

    List<UserResponseDto> getAllUsers();

    UserResponseDto getUserById(
            Long id
    );

    void deleteUser(
            Long id
    );
}
