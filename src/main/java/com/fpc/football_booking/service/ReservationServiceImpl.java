package com.fpc.football_booking.service;


import com.fpc.football_booking.dto.ReservationDto;
import com.fpc.football_booking.entity.AppUser;
import com.fpc.football_booking.entity.FootballField;
import com.fpc.football_booking.entity.Reservation;
import com.fpc.football_booking.entity.enums.ReservationStatus;
import com.fpc.football_booking.exception.BusinessException;
import com.fpc.football_booking.exception.ResourceNotFoundException;
import com.fpc.football_booking.mapper.ReservationMapper;
import com.fpc.football_booking.repository.AppUserRepository;
import com.fpc.football_booking.repository.FootballFieldRepository;
import com.fpc.football_booking.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationServiceImpl implements ReservationService {


    private final ReservationRepository reservationRepository;

    private final AppUserRepository userRepository;

    private final FootballFieldRepository fieldRepository;

    private final ReservationMapper reservationMapper;


    public ReservationServiceImpl(
            ReservationRepository reservationRepository,
            AppUserRepository userRepository,
            FootballFieldRepository fieldRepository,
            ReservationMapper reservationMapper
    ) {

        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.fieldRepository = fieldRepository;
        this.reservationMapper = reservationMapper;

    }


    @Override
    public ReservationDto createReservation(
            ReservationDto dto
    ) {


        // 1) Controllo orario apertura

        LocalTime opening = LocalTime.of(16, 0);
        LocalTime closing = LocalTime.MIDNIGHT;


        if(dto.getStartTime().isBefore(opening)
                || dto.getStartTime().equals(closing)) {

            throw new RuntimeException(
                    "Field closed at this time"
            );
        }



        // 2) Recupero utente

        AppUser user =
                userRepository.findById(dto.getUserId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "User not found"
                                ));



        // 3) Controllo una prenotazione al giorno

        boolean userAlreadyBooked =
                reservationRepository
                        .existsByUserIdAndReservationDate(
                                dto.getUserId(),
                                dto.getReservationDate()
                        );


        if(userAlreadyBooked){

            throw new com.fpc.football_booking.exception.BusinessException(
                    "User already has a reservation today"
            );

        }



        // 4) Recupero campo

        FootballField field =
                fieldRepository.findById(
                                dto.getFootballFieldId()
                        )
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Football field not found"
                                ));



        // 5) Controllo disponibilità campo

        boolean fieldAlreadyBooked =
                reservationRepository
                        .existsByFootballFieldIdAndReservationDateAndStartTime(
                                dto.getFootballFieldId(),
                                dto.getReservationDate(),
                                dto.getStartTime()
                        );


        if(fieldAlreadyBooked){

            throw new BusinessException(
                    "Football field already booked"
            );

        }



        // 6) Creo prenotazione

        Reservation reservation =
                reservationMapper.toEntity(dto);


        reservation.setUser(user);

        reservation.setFootballField(field);

        reservation.setStatus(
                ReservationStatus.CONFIRMED
        );



        // 7) Salvo

        Reservation saved =
                reservationRepository.save(
                        reservation
                );


        return reservationMapper.toDTO(saved);

    }



    @Override
    public List<ReservationDto> getUserReservations(
            Long userId
    ) {

        return reservationMapper.toDTOList(
                reservationRepository.findByUserId(userId)
        );

    }



    @Override
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
    public void cancelReservation(
            Long reservationId
    ) {


        Reservation reservation =
                reservationRepository.findById(reservationId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Reservation not found"
                                ));


        reservation.setStatus(
                ReservationStatus.CANCELLED
        );


        reservationRepository.save(reservation);

    }

}
