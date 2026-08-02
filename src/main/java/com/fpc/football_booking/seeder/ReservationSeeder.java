package com.fpc.football_booking.seeder;

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
@Order(2)
public class ReservationSeeder implements CommandLineRunner {


    private static final Logger logger =
            LoggerFactory.getLogger(ReservationSeeder.class);


    private static final BigDecimal FIXED_PRICE =
            new BigDecimal("50.00");



    private final ReservationRepository reservationRepository;

    private final FootballFieldRepository fieldRepository;



    public ReservationSeeder(
            ReservationRepository reservationRepository,
            FootballFieldRepository fieldRepository
    ) {

        this.reservationRepository = reservationRepository;
        this.fieldRepository = fieldRepository;

    }



    @Override
    public void run(String... args) {


        if(reservationRepository.count() == 0) {


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
                            "Mario Rossi",
                            "3331111111",
                            "mario@test.com",
                            field1,
                            LocalTime.of(18,0)
                    );



            Reservation reservation2 =
                    createReservation(
                            "Luca Bianchi",
                            "3332222222",
                            "luca@test.com",
                            field1,
                            LocalTime.of(20,0)
                    );



            Reservation reservation3 =
                    createReservation(
                            "Giuseppe Verdi",
                            "3333333333",
                            "giuseppe@test.com",
                            field2,
                            LocalTime.of(21,0)
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


        } else {


            logger.info(
                    "ReservationSeeder skipped: database already contains data"
            );

        }


    }





    private Reservation createReservation(
            String customerName,
            String customerPhone,
            String customerEmail,
            FootballField field,
            LocalTime startTime
    ) {


        Reservation reservation = new Reservation();



        reservation.setCustomerName(
                customerName
        );


        reservation.setCustomerPhone(
                customerPhone
        );


        reservation.setCustomerEmail(
                customerEmail
        );



        reservation.setFootballField(
                field
        );



        reservation.setReservationDate(
                LocalDate.now().plusDays(1)
        );



        reservation.setStartTime(
                startTime
        );



        reservation.setPrice(
                FIXED_PRICE
        );



        reservation.setStatus(
                ReservationStatus.CONFIRMED
        );



        return reservation;

    }

}