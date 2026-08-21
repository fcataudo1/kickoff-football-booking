package com.fpc.football_booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fpc.football_booking.dto.ReservationDto;
import com.fpc.football_booking.entity.enums.Role;
import com.fpc.football_booking.entity.User;
import com.fpc.football_booking.service.ReservationService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private ReservationService reservationService;

    private ReservationController reservationController;

    private User user;

    private Authentication authentication;


    // =================================================
    // SETUP
    // =================================================

    @BeforeEach
    void setUp() {

        reservationController =
                new ReservationController(
                        reservationService
                );

        mockMvc =
                MockMvcBuilders
                        .standaloneSetup(
                                reservationController
                        )
                        .build();

        objectMapper =
                new ObjectMapper();

        objectMapper.registerModule(
                new JavaTimeModule()
        );


        user =
                new User();

        user.setId(1L);
        user.setNome("Mario");
        user.setCognome("Rossi");
        user.setEmail("mario@email.it");
        user.setTelefono("3331234567");
        user.setRuolo(Role.CLIENTE);


        authentication =
                new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        List.of()
                );
    }


    // =================================================
    // MOCK RESERVATION
    // =================================================

    private ReservationDto createReservationDto() {

        ReservationDto dto =
                new ReservationDto();

        dto.setId(1L);
        dto.setReservationDate(
                LocalDate.of(2026, 8, 25)
        );
        dto.setStartTime(
                LocalTime.of(18, 0)
        );
        dto.setPrice(
                BigDecimal.valueOf(50)
        );

        return dto;
    }


    // =================================================
    // CREATE
    // =================================================

    @Test
    void shouldCreateReservation()
            throws Exception {

        ReservationDto request =
                createReservationDto();

        ReservationDto response =
                createReservationDto();


        when(
                reservationService.createReservation(
                        any(ReservationDto.class),
                        eq(1L)
                )
        )
                .thenReturn(response);


        mockMvc.perform(
                        post("/api/reservations")
                                .principal(authentication)
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
                        jsonPath("$.reservationDate")
                                .value("2026-08-25")
                )
                .andExpect(
                        jsonPath("$.startTime")
                                .value("18:00:00")
                )
                .andExpect(
                        jsonPath("$.price")
                                .value(50)
                );


        verify(
                reservationService
        )
                .createReservation(
                        any(ReservationDto.class),
                        eq(1L)
                );
    }


    // =================================================
    // CREATE - USER ID
    // =================================================

    @Test
    void shouldUseAuthenticatedUserIdWhenCreatingReservation()
            throws Exception {

        ReservationDto request =
                createReservationDto();


        when(
                reservationService.createReservation(
                        any(ReservationDto.class),
                        eq(1L)
                )
        )
                .thenReturn(request);


        mockMvc.perform(
                        post("/api/reservations")
                                .principal(authentication)
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
                reservationService
        )
                .createReservation(
                        any(ReservationDto.class),
                        eq(1L)
                );
    }


    // =================================================
    // GET ALL
    // =================================================

    @Test
    void shouldGetAllReservations()
            throws Exception {

        ReservationDto reservation =
                createReservationDto();


        when(
                reservationService.getAllReservations()
        )
                .thenReturn(
                        List.of(reservation)
                );


        mockMvc.perform(
                        get("/api/reservations")
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
                        jsonPath("$[0].reservationDate")
                                .value("2026-08-25")
                );


        verify(
                reservationService
        )
                .getAllReservations();
    }


    // =================================================
    // GET MY RESERVATIONS
    // =================================================

    @Test
    void shouldGetMyReservations()
            throws Exception {

        ReservationDto reservation =
                createReservationDto();


        when(
                reservationService.getMyReservations(1L)
        )
                .thenReturn(
                        List.of(reservation)
                );


        mockMvc.perform(
                        get("/api/reservations/my")
                                .principal(authentication)
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
                );


        verify(
                reservationService
        )
                .getMyReservations(1L);
    }


    // =================================================
    // GET BY FIELD
    // =================================================

    @Test
    void shouldGetReservationsByField()
            throws Exception {

        ReservationDto reservation =
                createReservationDto();


        when(
                reservationService.getFieldReservations(
                        1L,
                        LocalDate.of(2026, 8, 25)
                )
        )
                .thenReturn(
                        List.of(reservation)
                );


        mockMvc.perform(
                        get("/api/reservations/field/1")
                                .param(
                                        "date",
                                        "2026-08-25"
                                )
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
                );


        verify(
                reservationService
        )
                .getFieldReservations(
                        1L,
                        LocalDate.of(2026, 8, 25)
                );
    }


    // =================================================
    // CANCEL USER
    // =================================================

    @Test
    void shouldCancelReservation()
            throws Exception {

        doNothing()
                .when(
                        reservationService
                )
                .cancelReservation(
                        1L,
                        1L
                );


        mockMvc.perform(
                        delete("/api/reservations/1")
                                .principal(authentication)
                )
                .andExpect(
                        status().isNoContent()
                );


        verify(
                reservationService
        )
                .cancelReservation(
                        1L,
                        1L
                );
    }


    // =================================================
    // CANCEL BY STAFF
    // =================================================

    @Test
    void shouldCancelReservationByStaff()
            throws Exception {

        doNothing()
                .when(
                        reservationService
                )
                .cancelReservationByStaff(1L);


        mockMvc.perform(
                        patch(
                                "/api/reservations/1/cancel"
                        )
                )
                .andExpect(
                        status().isNoContent()
                );


        verify(
                reservationService
        )
                .cancelReservationByStaff(1L);
    }





}