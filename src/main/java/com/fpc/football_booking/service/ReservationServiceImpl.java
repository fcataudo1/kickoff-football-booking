package com.fpc.football_booking.service;


import com.fpc.football_booking.dto.ReservationDto;
import com.fpc.football_booking.entity.FootballField;
import com.fpc.football_booking.entity.Reservation;
import com.fpc.football_booking.entity.enums.ReservationStatus;
import com.fpc.football_booking.exception.BusinessException;
import com.fpc.football_booking.exception.ResourceNotFoundException;
import com.fpc.football_booking.mapper.ReservationMapper;
import com.fpc.football_booking.repository.FootballFieldRepository;
import com.fpc.football_booking.repository.ReservationRepository;
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

    private final ReservationMapper reservationMapper;

    private static final BigDecimal FIXED_PRICE =
            BigDecimal.valueOf(50);

    public ReservationServiceImpl(
            ReservationRepository reservationRepository,
            FootballFieldRepository fieldRepository,
            ReservationMapper reservationMapper
    ) {

        this.reservationRepository = reservationRepository;
        this.fieldRepository = fieldRepository;
        this.reservationMapper = reservationMapper;

    }



    @Override
    @Transactional
    public ReservationDto createReservation(
            ReservationDto dto
    ) {


        validateReservationTime(
                dto.getStartTime()
        );



        FootballField field =
                fieldRepository.findById(
                                dto.getFootballFieldId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Football field not found"
                                )
                        );



        if(!field.isActive()) {

            throw new BusinessException(
                    "Football field is not available"
            );

        }



        boolean alreadyBooked =
                reservationRepository
                        .existsByFootballFieldIdAndReservationDateAndStartTime(
                                dto.getFootballFieldId(),
                                dto.getReservationDate(),
                                dto.getStartTime()
                        );


        if(alreadyBooked){

            throw new BusinessException(
                    "Football field already booked"
            );

        }



        Reservation reservation =
                reservationMapper.toEntity(dto);



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
    @Transactional
    public void cancelReservation(Long reservationId) {

        Reservation reservation =
                reservationRepository.findById(reservationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reservation not found"
                                )
                        );


        if(reservation.getStatus() == ReservationStatus.CANCELLED) {

            throw new BusinessException(
                    "Reservation already cancelled"
            );

        }


        reservation.setStatus(
                ReservationStatus.CANCELLED
        );


        reservationRepository.save(reservation);

    }





    private void validateReservationTime(
            LocalTime startTime
    ) {


        LocalTime opening =
                LocalTime.of(16,0);


        LocalTime lastSlot =
                LocalTime.of(23,0);



        if(startTime.isBefore(opening)
                || startTime.isAfter(lastSlot)) {


            throw new BusinessException(
                    "Reservations are available from 16:00 to 23:00"
            );

        }



        if(startTime.getMinute() != 0
                || startTime.getSecond() != 0
                || startTime.getNano() != 0) {


            throw new BusinessException(
                    "Reservation must start on a full hour"
            );

        }

    }

}