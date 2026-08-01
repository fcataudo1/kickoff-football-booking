package com.fpc.football_booking.seeder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fpc.football_booking.entity.AppUser;
import com.fpc.football_booking.entity.FootballField;
import com.fpc.football_booking.entity.Reservation;
import com.fpc.football_booking.entity.enums.ReservationStatus;
import com.fpc.football_booking.repository.AppUserRepository;
import com.fpc.football_booking.repository.FootballFieldRepository;
import com.fpc.football_booking.repository.ReservationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@Profile("dev")
@Order(3)
public class ReservationSeeder implements CommandLineRunner {


    private static final Logger logger =
            LoggerFactory.getLogger(ReservationSeeder.class);


    private final ReservationRepository reservationRepository;

    private final AppUserRepository userRepository;

    private final FootballFieldRepository fieldRepository;


    public ReservationSeeder(
            ReservationRepository reservationRepository,
            AppUserRepository userRepository,
            FootballFieldRepository fieldRepository
    ) {

        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.fieldRepository = fieldRepository;

    }


    @Override
    public void run(String... args) {


        if(reservationRepository.count() == 0) {


            AppUser user2 =
                    userRepository
                            .findByEmail("user2@example.com")
                            .orElseThrow();


            AppUser user3 =
                    userRepository
                            .findByEmail("user3@example.com")
                            .orElseThrow();


            AppUser user4 =
                    userRepository
                            .findByEmail("user4@example.com")
                            .orElseThrow();



            FootballField field1 =
                    fieldRepository
                            .findByName("Campo 1")
                            .orElseThrow();


            FootballField field2 =
                    fieldRepository
                            .findByName("Campo 2")
                            .orElseThrow();



            Reservation reservation1 =
                    createReservation(
                            user2,
                            field1,
                            LocalTime.of(18, 0)
                    );


            Reservation reservation2 =
                    createReservation(
                            user3,
                            field1,
                            LocalTime.of(20, 0)
                    );


            Reservation reservation3 =
                    createReservation(
                            user4,
                            field2,
                            LocalTime.of(21, 0)
                    );


            reservationRepository.saveAll(
                    List.of(
                            reservation1,
                            reservation2,
                            reservation3
                    )
            );


            logger.info(
                    "ReservationSeeder initialized: 3 reservations created"
            );

        }

    }


    private Reservation createReservation(
            AppUser user,
            FootballField field,
            LocalTime startTime
    ) {


        Reservation reservation = new Reservation();


        reservation.setUser(user);


        reservation.setFootballField(field);


        reservation.setReservationDate(
                LocalDate.now().plusDays(1)
        );


        reservation.setStartTime(startTime);


        reservation.setStatus(
                ReservationStatus.CONFIRMED
        );


        return reservation;

    }

}