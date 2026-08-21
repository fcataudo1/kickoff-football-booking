package com.fpc.football_booking.service;


import com.fpc.football_booking.dto.ReservationDto;
import com.fpc.football_booking.entity.FootballField;
import com.fpc.football_booking.entity.Reservation;
import com.fpc.football_booking.entity.User;
import com.fpc.football_booking.exception.ConflictException;
import com.fpc.football_booking.exception.ResourceNotFoundException;
import com.fpc.football_booking.mapper.ReservationMapper;
import com.fpc.football_booking.repository.FootballFieldRepository;
import com.fpc.football_booking.repository.ReservationRepository;
import com.fpc.football_booking.entity.enums.ReservationStatus;
import com.fpc.football_booking.exception.BusinessException;
import com.fpc.football_booking.repository.UserRepository;
import com.fpc.football_booking.service.impl.ReservationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
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
    private UserRepository userRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationServiceImpl reservationService;


    private ReservationDto reservationDto;

    private Reservation reservation;

    private ReservationDto responseDto;

    private User user;

    private FootballField field;


    @BeforeEach
    void setUp() {

        reservationDto = new ReservationDto();

        reservationDto.setReservationDate(
                LocalDate.now().plusDays(5)
        );

        reservationDto.setStartTime(
                LocalTime.of(18, 0)
        );


        responseDto = new ReservationDto();


        user = new User();

        user.setId(1L);


        field = new FootballField();

        field.setId(1L);

        field.setName("Campo 1");

        field.setActive(true);


        reservation = new Reservation();

        reservation.setId(1L);

        reservation.setReservationDate(
                reservationDto.getReservationDate()
        );

        reservation.setStartTime(
                reservationDto.getStartTime()
        );

        reservation.setUser(user);

        reservation.setFootballField(field);

        reservation.setPrice(
                BigDecimal.valueOf(50)
        );

        reservation.setStatus(
                ReservationStatus.CONFIRMED
        );
    }


    // =========================================================
    // CREATE RESERVATION
    // =========================================================

    @Test
    void shouldCreateReservationSuccessfully() {

        when(userRepository.findById(1L))
                .thenReturn(
                        Optional.of(user)
                );


        when(
                reservationRepository
                        .existsConfirmedReservation(
                                1L,
                                reservationDto.getReservationDate(),
                                reservationDto.getStartTime()
                        )
        ).thenReturn(false);


        when(
                fieldRepository.findAvailableFields(
                        reservationDto.getReservationDate(),
                        reservationDto.getStartTime()
                )
        ).thenReturn(
                List.of(field)
        );


        when(
                reservationMapper.toEntity(
                        reservationDto
                )
        ).thenReturn(reservation);


        when(
                reservationRepository.save(
                        reservation
                )
        ).thenReturn(reservation);


        when(
                reservationMapper.toDTO(
                        reservation
                )
        ).thenReturn(responseDto);


        ReservationDto result =
                reservationService.createReservation(
                        reservationDto,
                        1L
                );


        assertNotNull(result);

        assertSame(
                responseDto,
                result
        );


        assertEquals(
                user,
                reservation.getUser()
        );


        assertEquals(
                field,
                reservation.getFootballField()
        );


        assertEquals(
                BigDecimal.valueOf(50),
                reservation.getPrice()
        );


        assertEquals(
                ReservationStatus.CONFIRMED,
                reservation.getStatus()
        );


        verify(userRepository)
                .findById(1L);


        verify(
                reservationRepository
        ).existsConfirmedReservation(
                1L,
                reservationDto.getReservationDate(),
                reservationDto.getStartTime()
        );


        verify(fieldRepository)
                .findAvailableFields(
                        reservationDto.getReservationDate(),
                        reservationDto.getStartTime()
                );


        verify(reservationRepository)
                .save(reservation);


        verify(reservationMapper)
                .toDTO(reservation);
    }


    // =========================================================
    // DATA NEL PASSATO
    // =========================================================

    @Test
    void shouldThrowExceptionWhenReservationDateIsInThePast() {

        reservationDto.setReservationDate(
                LocalDate.now().minusDays(1)
        );


        BusinessException exception =
                assertThrows(
                        BusinessException.class,
                        () ->
                                reservationService.createReservation(
                                        reservationDto,
                                        1L
                                )
                );


        assertEquals(
                "Reservation date cannot be in the past",
                exception.getMessage()
        );


        verifyNoInteractions(
                userRepository,
                reservationRepository,
                fieldRepository,
                reservationMapper
        );
    }


    // =========================================================
    // ORARIO PRIMA DELLE 16
    // =========================================================

    @Test
    void shouldRejectReservationBeforeOpeningTime() {

        reservationDto.setStartTime(
                LocalTime.of(15, 0)
        );


        BusinessException exception =
                assertThrows(
                        BusinessException.class,
                        () ->
                                reservationService.createReservation(
                                        reservationDto,
                                        1L
                                )
                );


        assertEquals(
                "Reservations are available from 16:00 to 23:00",
                exception.getMessage()
        );


        verifyNoInteractions(
                userRepository,
                reservationRepository,
                fieldRepository,
                reservationMapper
        );
    }


    // =========================================================
    // ORARIO DOPO LE 23
    // =========================================================

    @Test
    void shouldRejectReservationAfterLastSlot() {

        reservationDto.setStartTime(
                LocalTime.of(23, 30)
        );


        BusinessException exception =
                assertThrows(
                        BusinessException.class,
                        () ->
                                reservationService.createReservation(
                                        reservationDto,
                                        1L
                                )
                );


        assertEquals(
                "Reservations are available from 16:00 to 23:00",
                exception.getMessage()
        );


        verifyNoInteractions(
                userRepository,
                reservationRepository,
                fieldRepository,
                reservationMapper
        );
    }


    // =========================================================
    // ORARIO NON PIENO
    // =========================================================

    @Test
    void shouldRejectReservationNotOnFullHour() {

        reservationDto.setStartTime(
                LocalTime.of(18, 30)
        );


        BusinessException exception =
                assertThrows(
                        BusinessException.class,
                        () ->
                                reservationService.createReservation(
                                        reservationDto,
                                        1L
                                )
                );


        assertEquals(
                "Reservation must start on a full hour",
                exception.getMessage()
        );


        verifyNoInteractions(
                userRepository,
                reservationRepository,
                fieldRepository,
                reservationMapper
        );
    }


    // =========================================================
    // SECONDI DIVERSI DA ZERO
    // =========================================================

    @Test
    void shouldRejectReservationWithSeconds() {

        reservationDto.setStartTime(
                LocalTime.of(18, 0, 1)
        );


        BusinessException exception =
                assertThrows(
                        BusinessException.class,
                        () ->
                                reservationService.createReservation(
                                        reservationDto,
                                        1L
                                )
                );


        assertEquals(
                "Reservation must start on a full hour",
                exception.getMessage()
        );
    }


    // =========================================================
    // UTENTE NON TROVATO
    // =========================================================

    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        when(userRepository.findById(99L))
                .thenReturn(
                        Optional.empty()
                );


        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () ->
                                reservationService.createReservation(
                                        reservationDto,
                                        99L
                                )
                );


        assertEquals(
                "User not found",
                exception.getMessage()
        );


        verify(userRepository)
                .findById(99L);


        verify(
                reservationRepository,
                never()
        ).save(any());


        verify(
                fieldRepository,
                never()
        ).findAvailableFields(any(), any());
    }


    // =========================================================
    // PRENOTAZIONE GIÀ ESISTENTE
    // =========================================================

    @Test
    void shouldRejectDuplicateReservation() {

        when(userRepository.findById(1L))
                .thenReturn(
                        Optional.of(user)
                );


        when(
                reservationRepository
                        .existsConfirmedReservation(
                                1L,
                                reservationDto.getReservationDate(),
                                reservationDto.getStartTime()
                        )
        ).thenReturn(true);


        ConflictException exception =
                assertThrows(
                        ConflictException.class,
                        () ->
                                reservationService.createReservation(
                                        reservationDto,
                                        1L
                                )
                );


        assertEquals(
                "You already have a reservation for this time",
                exception.getMessage()
        );


        verify(
                fieldRepository,
                never()
        ).findAvailableFields(any(), any());


        verify(
                reservationRepository,
                never()
        ).save(any());
    }


    // =========================================================
    // NESSUN CAMPO DISPONIBILE
    // =========================================================

    @Test
    void shouldRejectReservationWhenNoFieldIsAvailable() {

        when(userRepository.findById(1L))
                .thenReturn(
                        Optional.of(user)
                );


        when(
                reservationRepository
                        .existsConfirmedReservation(
                                1L,
                                reservationDto.getReservationDate(),
                                reservationDto.getStartTime()
                        )
        ).thenReturn(false);


        when(
                fieldRepository.findAvailableFields(
                        reservationDto.getReservationDate(),
                        reservationDto.getStartTime()
                )
        ).thenReturn(
                List.of()
        );


        ConflictException exception =
                assertThrows(
                        ConflictException.class,
                        () ->
                                reservationService.createReservation(
                                        reservationDto,
                                        1L
                                )
                );


        assertEquals(
                "No football field available",
                exception.getMessage()
        );


        verify(
                reservationRepository,
                never()
        ).save(any());


        verify(
                reservationMapper,
                never()
        ).toEntity(any());
    }


    // =========================================================
    // GET FIELD RESERVATIONS
    // =========================================================

    @Test
    void shouldReturnFieldReservations() {

        LocalDate date =
                LocalDate.now().plusDays(5);


        List<Reservation> reservations =
                List.of(reservation);


        List<ReservationDto> dtoList =
                List.of(responseDto);


        when(
                reservationRepository
                        .findByFootballFieldIdAndReservationDate(
                                1L,
                                date
                        )
        ).thenReturn(reservations);


        when(
                reservationMapper.toDTOList(
                        reservations
                )
        ).thenReturn(dtoList);


        List<ReservationDto> result =
                reservationService.getFieldReservations(
                        1L,
                        date
                );


        assertEquals(
                dtoList,
                result
        );


        verify(
                reservationRepository
        ).findByFootballFieldIdAndReservationDate(
                1L,
                date
        );


        verify(
                reservationMapper
        ).toDTOList(reservations);
    }


    // =========================================================
    // GET ALL RESERVATIONS
    // =========================================================

    @Test
    void shouldReturnAllReservations() {

        List<Reservation> reservations =
                List.of(reservation);


        List<ReservationDto> dtoList =
                List.of(responseDto);


        when(
                reservationRepository.findAll()
        ).thenReturn(reservations);


        when(
                reservationMapper.toDTOList(
                        reservations
                )
        ).thenReturn(dtoList);


        List<ReservationDto> result =
                reservationService.getAllReservations();


        assertEquals(
                dtoList,
                result
        );


        verify(
                reservationRepository
        ).findAll();


        verify(
                reservationMapper
        ).toDTOList(reservations);
    }


    // =========================================================
    // GET MY RESERVATIONS
    // =========================================================

    @Test
    void shouldReturnMyReservations() {

        List<Reservation> reservations =
                List.of(reservation);


        List<ReservationDto> dtoList =
                List.of(responseDto);


        when(
                reservationRepository.findByUserId(1L)
        ).thenReturn(reservations);


        when(
                reservationMapper.toDTOList(
                        reservations
                )
        ).thenReturn(dtoList);


        List<ReservationDto> result =
                reservationService.getMyReservations(1L);


        assertEquals(
                dtoList,
                result
        );


        verify(
                reservationRepository
        ).findByUserId(1L);


        verify(
                reservationMapper
        ).toDTOList(reservations);
    }


    // =========================================================
    // CANCEL USER RESERVATION
    // =========================================================

    @Test
    void shouldCancelOwnReservation() {

        reservation.setStatus(
                ReservationStatus.CONFIRMED
        );


        when(
                reservationRepository.findById(1L)
        ).thenReturn(
                Optional.of(reservation)
        );


        reservationService.cancelReservation(
                1L,
                1L
        );


        assertEquals(
                ReservationStatus.CANCELLED,
                reservation.getStatus()
        );


        verify(
                reservationRepository
        ).save(reservation);
    }


    // =========================================================
    // RESERVATION NOT FOUND
    // =========================================================

    @Test
    void shouldThrowExceptionWhenCancellingNonExistingReservation() {

        when(
                reservationRepository.findById(99L)
        ).thenReturn(
                Optional.empty()
        );


        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () ->
                                reservationService.cancelReservation(
                                        99L,
                                        1L
                                )
                );


        assertEquals(
                "Reservation not found",
                exception.getMessage()
        );


        verify(
                reservationRepository,
                never()
        ).save(any());
    }


    // =========================================================
    // CANCELLED RESERVATION
    // =========================================================

    @Test
    void shouldRejectAlreadyCancelledReservation() {

        reservation.setStatus(
                ReservationStatus.CANCELLED
        );


        when(
                reservationRepository.findById(1L)
        ).thenReturn(
                Optional.of(reservation)
        );


        BusinessException exception =
                assertThrows(
                        BusinessException.class,
                        () ->
                                reservationService.cancelReservation(
                                        1L,
                                        1L
                                )
                );


        assertEquals(
                "Reservation already cancelled",
                exception.getMessage()
        );


        verify(
                reservationRepository,
                never()
        ).save(any());
    }


    // =========================================================
    // CANCEL OTHER USER RESERVATION
    // =========================================================

    @Test
    void shouldRejectCancellationOfAnotherUserReservation() {

        User otherUser =
                new User();

        otherUser.setId(2L);


        reservation.setUser(otherUser);

        reservation.setStatus(
                ReservationStatus.CONFIRMED
        );


        when(
                reservationRepository.findById(1L)
        ).thenReturn(
                Optional.of(reservation)
        );


        BusinessException exception =
                assertThrows(
                        BusinessException.class,
                        () ->
                                reservationService.cancelReservation(
                                        1L,
                                        1L
                                )
                );


        assertEquals(
                "You can only cancel your own reservations",
                exception.getMessage()
        );


        verify(
                reservationRepository,
                never()
        ).save(any());
    }


    // =========================================================
    // CANCEL BY STAFF
    // =========================================================

    @Test
    void shouldCancelReservationByStaff() {

        reservation.setStatus(
                ReservationStatus.CONFIRMED
        );


        when(
                reservationRepository.findById(1L)
        ).thenReturn(
                Optional.of(reservation)
        );


        reservationService.cancelReservationByStaff(1L);


        assertEquals(
                ReservationStatus.CANCELLED,
                reservation.getStatus()
        );


        verify(
                reservationRepository
        ).save(reservation);
    }


    // =========================================================
    // STAFF - RESERVATION NOT FOUND
    // =========================================================

    @Test
    void shouldThrowExceptionWhenStaffCancelsNonExistingReservation() {

        when(
                reservationRepository.findById(99L)
        ).thenReturn(
                Optional.empty()
        );


        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () ->
                                reservationService
                                        .cancelReservationByStaff(99L)
                );


        assertEquals(
                "Reservation not found",
                exception.getMessage()
        );


        verify(
                reservationRepository,
                never()
        ).save(any());
    }


    // =========================================================
    // STAFF - ALREADY CANCELLED
    // =========================================================

    @Test
    void shouldRejectStaffCancellationOfAlreadyCancelledReservation() {

        reservation.setStatus(
                ReservationStatus.CANCELLED
        );


        when(
                reservationRepository.findById(1L)
        ).thenReturn(
                Optional.of(reservation)
        );


        BusinessException exception =
                assertThrows(
                        BusinessException.class,
                        () ->
                                reservationService
                                        .cancelReservationByStaff(1L)
                );


        assertEquals(
                "Reservation already cancelled",
                exception.getMessage()
        );


        verify(
                reservationRepository,
                never()
        ).save(any());
    }

}
