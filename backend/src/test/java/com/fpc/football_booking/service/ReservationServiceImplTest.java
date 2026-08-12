package com.fpc.football_booking.service;


import com.fpc.football_booking.dto.ReservationDto;
import com.fpc.football_booking.entity.FootballField;
import com.fpc.football_booking.entity.Reservation;
import com.fpc.football_booking.exception.ConflictException;
import com.fpc.football_booking.mapper.ReservationMapper;
import com.fpc.football_booking.repository.FootballFieldRepository;
import com.fpc.football_booking.repository.ReservationRepository;
import com.fpc.football_booking.entity.enums.ReservationStatus;
import com.fpc.football_booking.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private FootballFieldRepository fieldRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationServiceImpl reservationService;


    @Test
    void shouldCreateReservation() {

        ReservationDto dto = new ReservationDto();

        dto.setCustomerName("Mario Rossi");
        dto.setCustomerPhone("3331111111");
        dto.setCustomerEmail("mario@test.com");
        dto.setReservationDate(
                LocalDate.of(2026, 8, 20)
        );
        dto.setStartTime(
                LocalTime.of(18, 0)
        );


        FootballField field = new FootballField();

        field.setId(1L);
        field.setName("Campo 1");
        field.setActive(true);


        Reservation reservation = new Reservation();

        reservation.setCustomerName("Mario Rossi");
        reservation.setCustomerPhone("3331111111");
        reservation.setCustomerEmail("mario@test.com");
        reservation.setReservationDate(
                LocalDate.of(2026, 8, 20)
        );
        reservation.setStartTime(
                LocalTime.of(18, 0)
        );


        Reservation savedReservation = new Reservation();

        savedReservation.setId(1L);
        savedReservation.setCustomerName("Mario Rossi");
        savedReservation.setCustomerPhone("3331111111");
        savedReservation.setCustomerEmail("mario@test.com");
        savedReservation.setReservationDate(
                LocalDate.of(2026, 8, 20)
        );
        savedReservation.setStartTime(
                LocalTime.of(18, 0)
        );
        savedReservation.setFootballField(field);
        savedReservation.setPrice(
                BigDecimal.valueOf(50)
        );
        savedReservation.setStatus(
                ReservationStatus.CONFIRMED
        );


        ReservationDto resultDto = new ReservationDto();

        resultDto.setId(1L);
        resultDto.setCustomerName("Mario Rossi");
        resultDto.setCustomerPhone("3331111111");
        resultDto.setReservationDate(
                LocalDate.of(2026, 8, 20)
        );
        resultDto.setStartTime(
                LocalTime.of(18, 0)
        );
        resultDto.setPrice(
                BigDecimal.valueOf(50)
        );
        resultDto.setStatus(
                ReservationStatus.CONFIRMED
        );


        when(
                fieldRepository.findAvailableFields(
                        LocalDate.of(2026, 8, 20),
                        LocalTime.of(18, 0)
                )
        ).thenReturn(List.of(field));


        when(
                reservationMapper.toEntity(dto)
        ).thenReturn(reservation);


        when(
                reservationRepository.save(any(Reservation.class))
        ).thenReturn(savedReservation);


        when(
                reservationMapper.toDTO(savedReservation)
        ).thenReturn(resultDto);


        ReservationDto result =
                reservationService.createReservation(dto);


        assertNotNull(result);

        assertEquals(
                1L,
                result.getId()
        );

        assertEquals(
                "Mario Rossi",
                result.getCustomerName()
        );

        assertEquals(
                BigDecimal.valueOf(50),
                result.getPrice()
        );

        assertEquals(
                ReservationStatus.CONFIRMED,
                result.getStatus()
        );


        verify(fieldRepository)
                .findAvailableFields(
                        LocalDate.of(2026, 8, 20),
                        LocalTime.of(18, 0)
                );

        verify(reservationRepository)
                .save(any(Reservation.class));

        verify(reservationMapper)
                .toEntity(dto);

        verify(reservationMapper)
                .toDTO(savedReservation);
    }

    @Test
    void shouldThrowExceptionWhenNoFieldIsAvailable() {

        ReservationDto dto = new ReservationDto();

        dto.setCustomerName("Mario Rossi");
        dto.setCustomerPhone("3331111111");
        dto.setCustomerEmail("mario@test.com");
        dto.setReservationDate(
                LocalDate.of(2026, 8, 20)
        );
        dto.setStartTime(
                LocalTime.of(18, 0)
        );


        when(
                fieldRepository.findAvailableFields(
                        LocalDate.of(2026, 8, 20),
                        LocalTime.of(18, 0)
                )
        ).thenReturn(List.of());


        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> reservationService.createReservation(dto)
        );


        assertEquals(
                "No football field available",
                exception.getMessage()
        );


        verify(fieldRepository)
                .findAvailableFields(
                        LocalDate.of(2026, 8, 20),
                        LocalTime.of(18, 0)
                );


        verify(reservationRepository, never())
                .save(any(Reservation.class));
    }

    @Test
    void shouldThrowExceptionWhenCustomerAlreadyHasReservation() {

        ReservationDto dto = new ReservationDto();

        dto.setCustomerName("Mario Rossi");
        dto.setCustomerPhone("3331111111");
        dto.setCustomerEmail("mario@test.com");
        dto.setReservationDate(
                LocalDate.of(2026, 8, 20)
        );
        dto.setStartTime(
                LocalTime.of(18, 0)
        );


        when(
                reservationRepository.existsConfirmedReservation(
                        "3331111111",
                        LocalDate.of(2026, 8, 20),
                        LocalTime.of(18, 0)
                )
        ).thenReturn(true);


        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> reservationService.createReservation(dto)
        );


        assertEquals(
                "You already have a reservation for this time",
                exception.getMessage()
        );


        verify(reservationRepository)
                .existsConfirmedReservation(
                        "3331111111",
                        LocalDate.of(2026, 8, 20),
                        LocalTime.of(18, 0)
                );


        verify(fieldRepository, never())
                .findAvailableFields(any(), any());


        verify(reservationRepository, never())
                .save(any(Reservation.class));
    }

    @Test
    void shouldThrowExceptionWhenReservationDateIsInThePast() {

        ReservationDto dto = new ReservationDto();

        dto.setCustomerName("Mario Rossi");
        dto.setCustomerPhone("3331111111");
        dto.setCustomerEmail("mario@test.com");
        dto.setReservationDate(
                LocalDate.now().minusDays(1)
        );
        dto.setStartTime(
                LocalTime.of(18, 0)
        );


        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> reservationService.createReservation(dto)
        );


        assertEquals(
                "Reservation date cannot be in the past",
                exception.getMessage()
        );


        verifyNoInteractions(
                reservationRepository,
                fieldRepository,
                reservationMapper
        );
    }

    @Test
    void shouldThrowExceptionWhenReservationIsBeforeOpeningTime() {

        ReservationDto dto = new ReservationDto();

        dto.setCustomerName("Mario Rossi");
        dto.setCustomerPhone("3331111111");
        dto.setCustomerEmail("mario@test.com");
        dto.setReservationDate(
                LocalDate.now().plusDays(1)
        );
        dto.setStartTime(
                LocalTime.of(15, 0)
        );


        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> reservationService.createReservation(dto)
        );


        assertEquals(
                "Reservations are available from 16:00 to 23:00",
                exception.getMessage()
        );


        verifyNoInteractions(
                reservationRepository,
                fieldRepository,
                reservationMapper
        );
    }


    @Test
    void shouldThrowExceptionWhenReservationIsAfterClosingTime() {

        ReservationDto dto = new ReservationDto();

        dto.setCustomerName("Mario Rossi");
        dto.setCustomerPhone("3331111111");
        dto.setCustomerEmail("mario@test.com");
        dto.setReservationDate(
                LocalDate.now().plusDays(1)
        );
        dto.setStartTime(
                LocalTime.of(23, 30)
        );


        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> reservationService.createReservation(dto)
        );


        assertEquals(
                "Reservations are available from 16:00 to 23:00",
                exception.getMessage()
        );


        verifyNoInteractions(
                reservationRepository,
                fieldRepository,
                reservationMapper
        );
    }

    @Test
    void shouldThrowExceptionWhenReservationIsNotOnFullHour() {

        ReservationDto dto = new ReservationDto();

        dto.setCustomerName("Mario Rossi");
        dto.setCustomerPhone("3331111111");
        dto.setCustomerEmail("mario@test.com");
        dto.setReservationDate(
                LocalDate.now().plusDays(1)
        );
        dto.setStartTime(
                LocalTime.of(18, 30)
        );


        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> reservationService.createReservation(dto)
        );


        assertEquals(
                "Reservation must start on a full hour",
                exception.getMessage()
        );


        verifyNoInteractions(
                reservationRepository,
                fieldRepository,
                reservationMapper
        );
    }


    @Test
    void shouldCancelReservation() {

        Reservation reservation = new Reservation();

        reservation.setId(1L);
        reservation.setStatus(
                ReservationStatus.CONFIRMED
        );


        when(
                reservationRepository.findById(1L)
        ).thenReturn(
                Optional.of(reservation)
        );


        reservationService.cancelReservation(1L);


        assertEquals(
                ReservationStatus.CANCELLED,
                reservation.getStatus()
        );


        verify(reservationRepository)
                .findById(1L);


        verify(reservationRepository)
                .save(reservation);
    }

    @Test
    void shouldThrowExceptionWhenReservationIsAlreadyCancelled() {

        Reservation reservation = new Reservation();

        reservation.setId(1L);
        reservation.setStatus(
                ReservationStatus.CANCELLED
        );


        when(
                reservationRepository.findById(1L)
        ).thenReturn(
                Optional.of(reservation)
        );


        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> reservationService.cancelReservation(1L)
        );


        assertEquals(
                "Reservation already cancelled",
                exception.getMessage()
        );


        verify(reservationRepository)
                .findById(1L);


        verify(reservationRepository, never())
                .save(any(Reservation.class));
    }
}