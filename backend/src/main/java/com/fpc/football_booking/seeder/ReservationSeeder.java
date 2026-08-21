package com.fpc.football_booking.seeder;

import com.fpc.football_booking.entity.User;
import com.fpc.football_booking.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fpc.football_booking.entity.FootballField;
import com.fpc.football_booking.entity.Reservation;
import com.fpc.football_booking.entity.enums.ReservationStatus;
import com.fpc.football_booking.repository.FootballFieldRepository;
import com.fpc.football_booking.repository.ReservationRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Component
@Profile("dev")
@Order(3)
public class ReservationSeeder implements CommandLineRunner {

    private static final Logger logger =
            LoggerFactory.getLogger(ReservationSeeder.class);

    private static final BigDecimal FIXED_PRICE =
            new BigDecimal("50.00");

    private final ReservationRepository reservationRepository;
    private final FootballFieldRepository fieldRepository;
    private final UserRepository userRepository;


    public ReservationSeeder(
            ReservationRepository reservationRepository,
            FootballFieldRepository fieldRepository,
            UserRepository userRepository
    ) {
        this.reservationRepository = reservationRepository;
        this.fieldRepository = fieldRepository;
        this.userRepository = userRepository;
    }


    @Override
    public void run(String... args) {

        if (reservationRepository.count() == 0) {

            FootballField field1 =
                    fieldRepository
                            .findByName("Campo 1")
                            .orElseThrow();

            FootballField field2 =
                    fieldRepository
                            .findByName("Campo 2")
                            .orElseThrow();


            User cliente =
                    userRepository
                            .findByEmail("mario.rossi@example.com")
                            .orElseThrow();


            Reservation reservation1 =
                    createReservation(
                            cliente,
                            field1,
                            LocalTime.of(18, 0)
                    );


            Reservation reservation2 =
                    createReservation(
                            cliente,
                            field2,
                            LocalTime.of(20, 0)
                    );


            reservationRepository.saveAll(
                    List.of(
                            reservation1,
                            reservation2
                    )
            );


            logger.info(
                    "ReservationSeeder initialized: 2 reservations created"
            );

        } else {

            logger.info(
                    "ReservationSeeder skipped: database already contains data"
            );
        }
    }


    private Reservation createReservation(
            User user,
            FootballField field,
            LocalTime startTime
    ) {

        Reservation reservation =
                new Reservation();


        reservation.setUser(user);

        reservation.setFootballField(field);

        reservation.setReservationDate(
                LocalDate.now().plusDays(1)
        );

        reservation.setStartTime(startTime);

        reservation.setPrice(FIXED_PRICE);

        reservation.setStatus(
                ReservationStatus.CONFIRMED
        );


        return reservation;
    }
}