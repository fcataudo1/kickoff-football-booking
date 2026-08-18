package com.fpc.football_booking.service;

import com.fpc.football_booking.dto.LoginRequestDto;
import com.fpc.football_booking.dto.LoginResponseDto;
import com.fpc.football_booking.entity.User;
import com.fpc.football_booking.exception.BusinessException;
import com.fpc.football_booking.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    @Override
    @Transactional(readOnly = true)
    public LoginResponseDto login(
            LoginRequestDto dto
    ) {

        User user =
                userRepository.findByEmail(
                                dto.getEmail()
                        )
                        .orElseThrow(() ->
                                new BusinessException(
                                        "Invalid email or password"
                                )
                        );


        if (!passwordEncoder.matches(
                dto.getPassword(),
                user.getPassword()
        )) {

            throw new BusinessException(
                    "Invalid email or password"
            );
        }

        String token = jwtService.generateToken(user);

        return LoginResponseDto.builder()
                .id(user.getId())
                .nome(user.getNome())
                .cognome(user.getCognome())
                .email(user.getEmail())
                .telefono(user.getTelefono())
                .ruolo(user.getRuolo())
                .token(token)
                .build();
    }
}
