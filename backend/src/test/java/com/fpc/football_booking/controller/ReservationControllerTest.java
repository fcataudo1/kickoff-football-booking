package com.fpc.football_booking.controller;

import com.fpc.football_booking.dto.ReservationDto;
import com.fpc.football_booking.entity.enums.ReservationStatus;
import com.fpc.football_booking.exception.ConflictException;
import com.fpc.football_booking.service.ReservationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.http.MediaType;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;


@WebMvcTest(ReservationController.class)

class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ReservationService reservationService;


    @Test
    void shouldGetAllReservations() throws Exception {

        ReservationDto reservation = new ReservationDto();

        reservation.setId(1L);
        reservation.setCustomerName("Mario Rossi");
        reservation.setCustomerPhone("3331111111");
        reservation.setCustomerEmail("mario@test.com");
        reservation.setReservationDate(LocalDate.of(2026, 8, 12));
        reservation.setStartTime(LocalTime.of(18, 0));
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setPrice(new BigDecimal("50.00"));

        when(reservationService.getAllReservations())
                .thenReturn(List.of(reservation));

        mockMvc.perform(
                        get("/api/reservations")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].customerName").value("Mario Rossi"))
                .andExpect(jsonPath("$[0].customerPhone").value("3331111111"))
                .andExpect(jsonPath("$[0].status").value("CONFIRMED"))
                .andExpect(jsonPath("$[0].price").value(50.00));
    }

    @Test
    void shouldGetReservationsByField() throws Exception {

        ReservationDto reservation = new ReservationDto();

        reservation.setId(1L);
        reservation.setCustomerName("Mario Rossi");
        reservation.setCustomerPhone("3331111111");
        reservation.setCustomerEmail("mario@test.com");
        reservation.setReservationDate(LocalDate.of(2026, 8, 12));
        reservation.setStartTime(LocalTime.of(18, 0));
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setPrice(new BigDecimal("50.00"));

        when(reservationService.getFieldReservations(
                1L,
                LocalDate.of(2026, 8, 12)
        )).thenReturn(List.of(reservation));

        mockMvc.perform(
                        get("/api/reservations/field/1")
                                .param("date", "2026-08-12")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].customerName").value("Mario Rossi"))
                .andExpect(jsonPath("$[0].reservationDate").value("2026-08-12"))
                .andExpect(jsonPath("$[0].startTime").value("18:00:00"))
                .andExpect(jsonPath("$[0].status").value("CONFIRMED"))
                .andExpect(jsonPath("$[0].price").value(50.00));
    }

    @Test
    void shouldCreateReservation() throws Exception {

        ReservationDto reservation = new ReservationDto();

        reservation.setId(4L);
        reservation.setCustomerName("Nuovo Cliente");
        reservation.setCustomerPhone("3334444444");
        reservation.setCustomerEmail("nuovo@test.com");
        reservation.setReservationDate(LocalDate.of(2026, 8, 12));
        reservation.setStartTime(LocalTime.of(21, 0));
        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setPrice(new BigDecimal("50.00"));

        when(reservationService.createReservation(any(ReservationDto.class)))
                .thenReturn(reservation);

        mockMvc.perform(
                        post("/api/reservations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                    {
                                        "customerName": "Nuovo Cliente",
                                        "customerPhone": "3334444444",
                                        "customerEmail": "nuovo@test.com",
                                        "reservationDate": "2026-08-12",
                                        "startTime": "21:00:00"
                                    }
                                    """)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(4))
                .andExpect(jsonPath("$.customerName").value("Nuovo Cliente"))
                .andExpect(jsonPath("$.customerPhone").value("3334444444"))
                .andExpect(jsonPath("$.customerEmail").value("nuovo@test.com"))
                .andExpect(jsonPath("$.reservationDate").value("2026-08-12"))
                .andExpect(jsonPath("$.startTime").value("21:00:00"))
                .andExpect(jsonPath("$.status").value("CONFIRMED"))
                .andExpect(jsonPath("$.price").value(50.00));
    }

    @Test
    void shouldCancelReservation() throws Exception {

        doNothing()
                .when(reservationService)
                .cancelReservation(1L);

        mockMvc.perform(
                        delete("/api/reservations/1")
                )
                .andExpect(status().isNoContent());

        verify(reservationService)
                .cancelReservation(1L);
    }


    @Test
    void shouldReturnConflictWhenNoFootballFieldIsAvailable() throws Exception {

        when(reservationService.createReservation(any(ReservationDto.class)))
                .thenThrow(
                        new ConflictException(
                                "No football field available"
                        )
                );

        mockMvc.perform(
                        post("/api/reservations")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                {
                                    "customerName": "Mario Rossi",
                                    "customerPhone": "3331111111",
                                    "customerEmail": "mario@test.com",
                                    "reservationDate": "2026-08-13",
                                    "startTime": "19:00:00"
                                }
                                """)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("No football field available"));
    }


}