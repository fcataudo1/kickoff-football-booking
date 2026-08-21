package com.fpc.football_booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpc.football_booking.dto.UserRequestDto;
import com.fpc.football_booking.dto.UserResponseDto;
import com.fpc.football_booking.entity.enums.Role;
import com.fpc.football_booking.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    private UserController userController;


    // =================================================
    // SETUP
    // =================================================

    @BeforeEach
    void setUp() {

        userController =
                new UserController(
                        userService
                );

        mockMvc =
                MockMvcBuilders
                        .standaloneSetup(
                                userController
                        )
                        .build();

        objectMapper =
                new ObjectMapper();
    }


    // =================================================
    // MOCK USER REQUEST
    // =================================================

    private UserRequestDto createUserRequest() {

        UserRequestDto dto =
                new UserRequestDto();

        dto.setNome("Mario");
        dto.setCognome("Rossi");
        dto.setEmail("mario@email.it");
        dto.setTelefono("3331234567");
        dto.setPassword("Password123!");

        return dto;
    }


    // =================================================
    // MOCK USER RESPONSE
    // =================================================

    private UserResponseDto createUserResponse() {

        UserResponseDto dto =
                new UserResponseDto();

        dto.setId(1L);
        dto.setNome("Mario");
        dto.setCognome("Rossi");
        dto.setEmail("mario@email.it");
        dto.setTelefono("3331234567");
        dto.setRuolo(Role.CLIENTE);

        return dto;
    }


    // =================================================
    // CREATE
    // =================================================

    @Test
    void shouldCreateUser()
            throws Exception {

        UserRequestDto request =
                createUserRequest();

        UserResponseDto response =
                createUserResponse();


        when(
                userService.createUser(
                        any(UserRequestDto.class)
                )
        )
                .thenReturn(response);


        mockMvc.perform(
                        post("/api/users")
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
                        status().isCreated()
                )
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


        verify(
                userService
        )
                .createUser(
                        any(UserRequestDto.class)
                );
    }


    // =================================================
    // GET ALL
    // =================================================

    @Test
    void shouldGetAllUsers()
            throws Exception {

        UserResponseDto user =
                createUserResponse();


        when(
                userService.getAllUsers()
        )
                .thenReturn(
                        List.of(user)
                );


        mockMvc.perform(
                        get("/api/users")
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.length()")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$[0].id")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$[0].nome")
                                .value("Mario")
                )
                .andExpect(
                        jsonPath("$[0].email")
                                .value("mario@email.it")
                );


        verify(
                userService
        )
                .getAllUsers();
    }


    // =================================================
    // GET BY ID
    // =================================================

    @Test
    void shouldGetUserById()
            throws Exception {

        UserResponseDto response =
                createUserResponse();


        when(
                userService.getUserById(1L)
        )
                .thenReturn(response);


        mockMvc.perform(
                        get("/api/users/1")
                )
                .andExpect(
                        status().isOk()
                )
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
                        jsonPath("$.ruolo")
                                .value("CLIENTE")
                );


        verify(
                userService
        )
                .getUserById(1L);
    }


    // =================================================
    // DELETE
    // =================================================

    @Test
    void shouldDeleteUser()
            throws Exception {

        doNothing()
                .when(
                        userService
                )
                .deleteUser(1L);


        mockMvc.perform(
                        delete("/api/users/1")
                )
                .andExpect(
                        status().isNoContent()
                );


        verify(
                userService
        )
                .deleteUser(1L);
    }


    // =================================================
    // CREATE - VERIFICA DTO
    // =================================================

    @Test
    void shouldPassCorrectDtoToService()
            throws Exception {

        UserRequestDto request =
                createUserRequest();

        UserResponseDto response =
                createUserResponse();


        when(
                userService.createUser(
                        any(UserRequestDto.class)
                )
        )
                .thenReturn(response);


        mockMvc.perform(
                        post("/api/users")
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
                        status().isCreated()
                );


        verify(
                userService
        )
                .createUser(
                        argThat(dto ->
                                dto.getNome()
                                        .equals("Mario")
                                        &&
                                        dto.getCognome()
                                                .equals("Rossi")
                                        &&
                                        dto.getEmail()
                                                .equals("mario@email.it")
                                        &&
                                        dto.getTelefono()
                                                .equals("3331234567")
                        )
                );
    }
}