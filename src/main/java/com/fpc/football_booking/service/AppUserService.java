package com.fpc.football_booking.service;

import com.fpc.football_booking.dto.AppUserDto;
import com.fpc.football_booking.entity.AppUser;
import com.fpc.football_booking.mapper.AppUserMapper;
import com.fpc.football_booking.repository.AppUserRepository;
import org.springframework.stereotype.Service;

@Service
public class AppUserService
        extends AbstractService<AppUser, AppUserDto> {


    public AppUserService(
            AppUserRepository userRepository,
            AppUserMapper userMapper
    ) {

        super(userRepository, userMapper);

    }

}

