package com.fpc.football_booking.service;


import com.fpc.football_booking.dto.LoginRequestDto;
import com.fpc.football_booking.dto.LoginResponseDto;
import com.fpc.football_booking.entity.enums.Role;
import com.fpc.football_booking.entity.User;
import com.fpc.football_booking.exception.BusinessException;
import com.fpc.football_booking.repository.UserRepository;
import com.fpc.football_booking.service.impl.AuthServiceImpl;
import com.fpc.football_booking.service.impl.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;


    // =================================================
    // LOGIN CORRETTO
    // =================================================

    @Test
    void shouldLoginSuccessfully() {

        LoginRequestDto dto =
                new LoginRequestDto();

        dto.setEmail(
                "mario@email.it"
        );

        dto.setPassword(
                "password123"
        );


        User user =
                new User();

        user.setId(1L);

        user.setNome(
                "Mario"
        );

        user.setCognome(
                "Rossi"
        );

        user.setEmail(
                "mario@email.it"
        );

        user.setTelefono(
                "3331234567"
        );

        user.setPassword(
                "encodedPassword"
        );

        user.setRuolo(
                Role.CLIENTE
        );


        when(
                userRepository.findByEmail(
                        "mario@email.it"
                )
        )
                .thenReturn(
                        Optional.of(user)
                );


        when(
                passwordEncoder.matches(
                        "password123",
                        "encodedPassword"
                )
        )
                .thenReturn(true);


        when(
                jwtService.generateToken(user)
        )
                .thenReturn(
                        "jwt-token-test"
                );


        LoginResponseDto result =
                authService.login(dto);


        assertNotNull(
                result
        );

        assertEquals(
                1L,
                result.getId()
        );

        assertEquals(
                "Mario",
                result.getNome()
        );

        assertEquals(
                "Rossi",
                result.getCognome()
        );

        assertEquals(
                "mario@email.it",
                result.getEmail()
        );

        assertEquals(
                "3331234567",
                result.getTelefono()
        );

        assertEquals(
                Role.CLIENTE,
                result.getRuolo()
        );

        assertEquals(
                "jwt-token-test",
                result.getToken()
        );


        verify(
                userRepository
        )
                .findByEmail(
                        "mario@email.it"
                );


        verify(
                passwordEncoder
        )
                .matches(
                        "password123",
                        "encodedPassword"
                );


        verify(
                jwtService
        )
                .generateToken(user);
    }


    // =================================================
    // EMAIL NON ESISTENTE
    // =================================================

    @Test
    void shouldThrowExceptionWhenEmailDoesNotExist() {

        LoginRequestDto dto =
                new LoginRequestDto();

        dto.setEmail(
                "inesistente@email.it"
        );

        dto.setPassword(
                "password123"
        );


        when(
                userRepository.findByEmail(
                        "inesistente@email.it"
                )
        )
                .thenReturn(
                        Optional.empty()
                );


        BusinessException exception =
                assertThrows(
                        BusinessException.class,
                        () ->
                                authService.login(dto)
                );


        assertEquals(
                "Invalid email or password",
                exception.getMessage()
        );


        verify(
                userRepository
        )
                .findByEmail(
                        "inesistente@email.it"
                );


        verifyNoInteractions(
                passwordEncoder
        );


        verifyNoInteractions(
                jwtService
        );
    }


    // =================================================
    // PASSWORD ERRATA
    // =================================================

    @Test
    void shouldThrowExceptionWhenPasswordIsWrong() {

        LoginRequestDto dto =
                new LoginRequestDto();

        dto.setEmail(
                "mario@email.it"
        );

        dto.setPassword(
                "passwordErrata"
        );


        User user =
                new User();

        user.setId(1L);

        user.setEmail(
                "mario@email.it"
        );

        user.setPassword(
                "encodedPassword"
        );

        user.setRuolo(
                Role.CLIENTE
        );


        when(
                userRepository.findByEmail(
                        "mario@email.it"
                )
        )
                .thenReturn(
                        Optional.of(user)
                );


        when(
                passwordEncoder.matches(
                        "passwordErrata",
                        "encodedPassword"
                )
        )
                .thenReturn(false);


        BusinessException exception =
                assertThrows(
                        BusinessException.class,
                        () ->
                                authService.login(dto)
                );


        assertEquals(
                "Invalid email or password",
                exception.getMessage()
        );


        verify(
                userRepository
        )
                .findByEmail(
                        "mario@email.it"
                );


        verify(
                passwordEncoder
        )
                .matches(
                        "passwordErrata",
                        "encodedPassword"
                );


        verifyNoInteractions(
                jwtService
        );
    }
}