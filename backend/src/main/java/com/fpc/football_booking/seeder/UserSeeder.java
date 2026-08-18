package com.fpc.football_booking.seeder;

import com.fpc.football_booking.entity.User;
import com.fpc.football_booking.entity.enums.Role;
import com.fpc.football_booking.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;


@Component
@Profile("dev")
@Order(2)
public class UserSeeder implements CommandLineRunner {

    private static final Logger logger =
            LoggerFactory.getLogger(UserSeeder.class);

    private final UserRepository userRepository;


    public UserSeeder(
            UserRepository userRepository
    ) {
        this.userRepository = userRepository;
    }


    @Override
    public void run(String... args) {

        if (userRepository.count() > 0) {

            logger.info(
                    "UserSeeder skipped: database already contains users"
            );

            return;
        }


        User cliente = User.builder()
                .nome("Mario")
                .cognome("Rossi")
                .email("mario.rossi@example.com")
                .password("password")
                .telefono("3331111111")
                .ruolo(Role.CLIENTE)
                .build();


        User receptionist = User.builder()
                .nome("Luca")
                .cognome("Bianchi")
                .email("luca.bianchi@example.com")
                .password("password")
                .telefono("3332222222")
                .ruolo(Role.RECEPTIONIST)
                .build();


        User admin = User.builder()
                .nome("Francesco")
                .cognome("Cataudo")
                .email("admin@kickoff.com")
                .password("password")
                .telefono("3333333333")
                .ruolo(Role.ADMIN)
                .build();


        userRepository.saveAll(
                List.of(
                        cliente,
                        receptionist,
                        admin
                )
        );


        logger.info(
                "UserSeeder initialized: 3 users created"
        );
    }
}