package com.fpc.football_booking.service;

import com.fpc.football_booking.dto.LoginRequestDto;
import com.fpc.football_booking.dto.LoginResponseDto;

public interface AuthService {

    LoginResponseDto login(
            LoginRequestDto dto
    );

}