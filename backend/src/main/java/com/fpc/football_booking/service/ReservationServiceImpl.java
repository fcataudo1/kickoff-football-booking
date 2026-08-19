package com.fpc.football_booking.service;


import com.fpc.football_booking.dto.ReservationDto;
import com.fpc.football_booking.entity.FootballField;
import com.fpc.football_booking.entity.Reservation;
import com.fpc.football_booking.entity.User;
import com.fpc.football_booking.entity.enums.ReservationStatus;
import com.fpc.football_booking.exception.BusinessException;
import com.fpc.football_booking.exception.ConflictException;
import com.fpc.football_booking.exception.ResourceNotFoundException;
import com.fpc.football_booking.mapper.ReservationMapper;
import com.fpc.football_booking.repository.FootballFieldRepository;
import com.fpc.football_booking.repository.ReservationRepository;
import com.fpc.football_booking.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final FootballFieldRepository fieldRepository;
    private final UserRepository userRepository;
    private final ReservationMapper reservationMapper;

    private static final BigDecimal FIXED_PRICE =
            BigDecimal.valueOf(50);

    public ReservationServiceImpl(
            ReservationRepository reservationRepository,
            FootballFieldRepository fieldRepository,
            UserRepository userRepository,
            ReservationMapper reservationMapper
    ) {
        this.reservationRepository = reservationRepository;
        this.fieldRepository = fieldRepository;
        this.userRepository = userRepository;
        this.reservationMapper = reservationMapper;
    }


    @Override
    @Transactional
    public ReservationDto createReservation(
            ReservationDto dto,
            Long userId
    ) {

        if (dto.getReservationDate().isBefore(LocalDate.now())) {

            throw new BusinessException(
                    "Reservation date cannot be in the past"
            );
        }

        validateReservationTime(
                dto.getStartTime()
        );


        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );


        if (reservationRepository
                .existsConfirmedReservation(
                        userId,
                        dto.getReservationDate(),
                        dto.getStartTime()
                )) {

            throw new ConflictException(
                    "You already have a reservation for this time"
            );
        }


        List<FootballField> availableFields =
                fieldRepository.findAvailableFields(
                        dto.getReservationDate(),
                        dto.getStartTime()
                );


        if (availableFields.isEmpty()) {

            throw new ConflictException(
                    "No football field available"
            );
        }


        FootballField field =
                availableFields.get(0);


        Reservation reservation =
                reservationMapper.toEntity(dto);


        reservation.setUser(user);

        reservation.setFootballField(
                field
        );

        reservation.setPrice(
                FIXED_PRICE
        );

        reservation.setStatus(
                ReservationStatus.CONFIRMED
        );


        Reservation saved =
                reservationRepository.save(
                        reservation
                );


        return reservationMapper.toDTO(saved);
    }


    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getFieldReservations(
            Long fieldId,
            LocalDate date
    ) {

        return reservationMapper.toDTOList(
                reservationRepository
                        .findByFootballFieldIdAndReservationDate(
                                fieldId,
                                date
                        )
        );
    }


    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getAllReservations() {

        return reservationMapper.toDTOList(
                reservationRepository.findAll()
        );
    }

    @Override
    @Transactional(readOnly = true)
    public List<ReservationDto> getMyReservations(
            Long userId
    ) {

        return reservationMapper.toDTOList(
                reservationRepository.findByUserId(userId)
        );
    }


    @Override
    @Transactional
    public void cancelReservation(
            Long reservationId,
            Long userId
    ) {

        Reservation reservation =
                reservationRepository.findById(
                        reservationId
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Reservation not found"
                        )
                );


        if (reservation.getStatus()
                == ReservationStatus.CANCELLED) {

            throw new BusinessException(
                    "Reservation already cancelled"
            );
        }


        if (!reservation.getUser().getId()
                .equals(userId)) {

            throw new BusinessException(
                    "You can only cancel your own reservations"
            );
        }


        reservation.setStatus(
                ReservationStatus.CANCELLED
        );


        reservationRepository.save(
                reservation
        );
    }


    private void validateReservationTime(
            LocalTime startTime
    ) {

        LocalTime opening =
                LocalTime.of(16, 0);

        LocalTime lastSlot =
                LocalTime.of(23, 0);


        if (startTime.isBefore(opening)
                || startTime.isAfter(lastSlot)) {

            throw new BusinessException(
                    "Reservations are available from 16:00 to 23:00"
            );
        }


        if (startTime.getMinute() != 0
                || startTime.getSecond() != 0
                || startTime.getNano() != 0) {

            throw new BusinessException(
                    "Reservation must start on a full hour"
            );
        }
    }

    @Override
    @Transactional
    public void cancelReservationByStaff(
            Long reservationId
    ) {

        Reservation reservation =
                reservationRepository.findById(
                        reservationId
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Reservation not found"
                        )
                );


        if (reservation.getStatus()
                == ReservationStatus.CANCELLED) {

            throw new BusinessException(
                    "Reservation already cancelled"
            );
        }


        reservation.setStatus(
                ReservationStatus.CANCELLED
        );


        reservationRepository.save(
                reservation
        );
    }
}