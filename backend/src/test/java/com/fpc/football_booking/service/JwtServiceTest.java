package com.fpc.football_booking.service;

import com.fpc.football_booking.entity.User;

import com.fpc.football_booking.entity.enums.Role;
import com.fpc.football_booking.service.impl.JwtService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;



import static org.junit.jupiter.api.Assertions.*;


class JwtServiceTest {

    private JwtService jwtService;

    private User user;


    // =================================================
    // CHIAVE DI TEST
    // =================================================

    private static final String SECRET_KEY =
            "MDEyMzQ1Njc4OWFiY2RlZjAxMjM0NTY3ODlhYmNkZWY=";


    // =================================================
    // SETUP
    // =================================================

    @BeforeEach
    void setUp() {

        jwtService =
                new JwtService();


        ReflectionTestUtils.setField(
                jwtService,
                "secretKey",
                SECRET_KEY
        );


        ReflectionTestUtils.setField(
                jwtService,
                "expirationTime",
                3600000L
        );


        user =
                new User();

        user.setId(1L);

        user.setEmail(
                "mario@email.it"
        );

        user.setRuolo(
                Role.CLIENTE
        );
    }


    // =================================================
    // GENERAZIONE TOKEN
    // =================================================

    @Test
    void shouldGenerateToken() {

        String token =
                jwtService.generateToken(
                        user
                );


        assertNotNull(
                token
        );


        assertFalse(
                token.isBlank()
        );
    }


    // =================================================
    // ESTRAZIONE EMAIL
    // =================================================

    @Test
    void shouldExtractEmailFromToken() {

        String token =
                jwtService.generateToken(
                        user
                );


        String email =
                jwtService.extractEmail(
                        token
                );


        assertEquals(
                "mario@email.it",
                email
        );
    }


    // =================================================
    // TOKEN VALIDO
    // =================================================

    @Test
    void shouldValidateToken() {

        String token =
                jwtService.generateToken(
                        user
                );


        boolean valid =
                jwtService.isTokenValid(
                        token,
                        user
                );


        assertTrue(
                valid
        );
    }


    // =================================================
    // UTENTE DIVERSO
    // =================================================

    @Test
    void shouldRejectTokenForDifferentUser() {

        String token =
                jwtService.generateToken(
                        user
                );


        User differentUser =
                new User();

        differentUser.setId(
                2L
        );

        differentUser.setEmail(
                "altro@email.it"
        );

        differentUser.setRuolo(
                Role.CLIENTE
        );


        boolean valid =
                jwtService.isTokenValid(
                        token,
                        differentUser
                );


        assertFalse(
                valid
        );
    }


    // =================================================
    // TOKEN SCADUTO
    // =================================================

    @Test
    void shouldRejectExpiredToken() {

        String expiredToken =
                Jwts.builder()

                        .subject(
                                user.getEmail()
                        )

                        .claim(
                                "userId",
                                user.getId()
                        )

                        .claim(
                                "role",
                                user.getRuolo().name()
                        )

                        .issuedAt(
                                new Date(
                                        System.currentTimeMillis()
                                                - 7200000L
                                )
                        )

                        .expiration(
                                new Date(
                                        System.currentTimeMillis()
                                                - 3600000L
                                )
                        )

                        .signWith(
                                getSigningKey()
                        )

                        .compact();


        boolean valid =
                jwtService.isTokenValid(
                        expiredToken,
                        user
                );


        assertFalse(
                valid
        );
    }


    // =================================================
    // CHIAVE TEST
    // =================================================

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(
                        SECRET_KEY
                )
        );
    }
}