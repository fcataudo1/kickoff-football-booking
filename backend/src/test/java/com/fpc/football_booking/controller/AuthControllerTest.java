package com.fpc.football_booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpc.football_booking.dto.LoginRequestDto;
import com.fpc.football_booking.dto.LoginResponseDto;
import com.fpc.football_booking.entity.enums.Role;
import com.fpc.football_booking.entity.User;
import com.fpc.football_booking.exception.BusinessException;
import com.fpc.football_booking.exception.GlobalExceptionHandler;
import com.fpc.football_booking.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.core.Authentication;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private AuthService authService;

    @Mock
    private Authentication authentication;

    private AuthController authController;


    @BeforeEach
    void setUp() {

        authController =
                new AuthController(authService);

        mockMvc =
                MockMvcBuilders
                        .standaloneSetup(authController)
                        .setControllerAdvice(
                                new GlobalExceptionHandler()
                        )
                        .build();

        objectMapper =
                new ObjectMapper();
    }


    // =================================================
    // LOGIN SUCCESS
    // =================================================

    @Test
    void shouldLoginSuccessfully()
            throws Exception {

        LoginRequestDto request =
                new LoginRequestDto();

        request.setEmail("mario@email.it");
        request.setPassword("password123");


        LoginResponseDto response =
                LoginResponseDto.builder()
                        .id(1L)
                        .nome("Mario")
                        .cognome("Rossi")
                        .email("mario@email.it")
                        .telefono("3331234567")
                        .ruolo(Role.CLIENTE)
                        .token("jwt-token")
                        .build();


        when(
                authService.login(
                        any(LoginRequestDto.class)
                )
        ).thenReturn(response);


        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.nome")
                                .value("Mario")
                )
                .andExpect(
                        jsonPath("$.cognome")
                                .value("Rossi")
                )
                .andExpect(
                        jsonPath("$.email")
                                .value("mario@email.it")
                )
                .andExpect(
                        jsonPath("$.telefono")
                                .value("3331234567")
                )
                .andExpect(
                        jsonPath("$.ruolo")
                                .value("CLIENTE")
                )
                .andExpect(
                        jsonPath("$.token")
                                .value("jwt-token")
                );


        verify(authService)
                .login(any(LoginRequestDto.class));
    }


    // =================================================
    // LOGIN ERRORE
    // =================================================

    @Test
    void shouldThrowExceptionWhenLoginFails()
            throws Exception {

        LoginRequestDto request =
                new LoginRequestDto();

        request.setEmail("wrong@email.it");
        request.setPassword("wrongpassword");


        when(
                authService.login(
                        any(LoginRequestDto.class)
                )
        ).thenThrow(
                new BusinessException(
                        "Invalid email or password"
                )
        );


        mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(
                        status().isBadRequest()
                )
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Invalid email or password"
                                )
                );


        verify(authService)
                .login(any(LoginRequestDto.class));
    }


    // =================================================
    // ME SUCCESS
    // =================================================

    @Test
    void shouldReturnAuthenticatedUser()
            throws Exception {

        User user =
                new User();

        user.setId(1L);
        user.setNome("Mario");
        user.setCognome("Rossi");
        user.setEmail("mario@email.it");
        user.setTelefono("3331234567");
        user.setRuolo(Role.CLIENTE);


        when(
                authentication.isAuthenticated()
        ).thenReturn(true);

        when(
                authentication.getPrincipal()
        ).thenReturn(user);


        mockMvc.perform(
                        get("/api/auth/me")
                                .principal(authentication)
                )
                .andExpect(status().isOk())
                .andExpect(
                        jsonPath("$.id")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.nome")
                                .value("Mario")
                )
                .andExpect(
                        jsonPath("$.cognome")
                                .value("Rossi")
                )
                .andExpect(
                        jsonPath("$.email")
                                .value("mario@email.it")
                )
                .andExpect(
                        jsonPath("$.telefono")
                                .value("3331234567")
                )
                .andExpect(
                        jsonPath("$.ruolo")
                                .value("CLIENTE")
                );


        verify(authentication)
                .isAuthenticated();

        verify(authentication)
                .getPrincipal();
    }


    // =================================================
    // ME SENZA AUTENTICAZIONE
    // =================================================

    @Test
    void shouldThrowExceptionWhenAuthenticationIsMissing()
            throws Exception {

        mockMvc.perform(
                        get("/api/auth/me")
                )
                .andExpect(
                        status().isBadRequest()
                )
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Authentication required"
                                )
                );


        verifyNoInteractions(authService);
    }


    // =================================================
    // ME AUTENTICAZIONE NON VALIDA
    // =================================================

    @Test
    void shouldThrowExceptionWhenAuthenticationIsNotAuthenticated()
            throws Exception {

        when(
                authentication.isAuthenticated()
        ).thenReturn(false);


        mockMvc.perform(
                        get("/api/auth/me")
                                .principal(authentication)
                )
                .andExpect(
                        status().isBadRequest()
                )
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Authentication required"
                                )
                );


        verify(authentication)
                .isAuthenticated();

        verify(authentication, never())
                .getPrincipal();
    }
}
