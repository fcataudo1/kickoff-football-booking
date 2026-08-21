package com.fpc.football_booking.service.impl;

import com.fpc.football_booking.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import io.jsonwebtoken.ExpiredJwtException;


@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expirationTime;


    // =================================================
    // GENERAZIONE TOKEN
    // =================================================

    public String generateToken(User user) {

        return Jwts.builder()
                .subject(user.getEmail())

                .claim(
                        "userId",
                        user.getId()
                )

                .claim(
                        "role",
                        user.getRuolo().name()
                )

                .issuedAt(
                        new Date()
                )

                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + expirationTime
                        )
                )

                .signWith(
                        getSigningKey()
                )

                .compact();
    }


    // =================================================
    // ESTRAZIONE EMAIL
    // =================================================

    public String extractEmail(String token) {

        return Jwts.parser()
                .verifyWith(
                        getSigningKey()
                )
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }


    // =================================================
    // VALIDAZIONE TOKEN
    // =================================================

    public boolean isTokenValid(
            String token,
            User user
    ) {

        try {

            String email =
                    extractEmail(token);

            return email.equals(
                    user.getEmail()
            );

        } catch (ExpiredJwtException e) {

            return false;
        }
    }


    // =================================================
    // CONTROLLO SCADENZA
    // =================================================

    private boolean isTokenExpired(
            String token
    ) {

        Date expiration =
                Jwts.parser()
                        .verifyWith(
                                getSigningKey()
                        )
                        .build()
                        .parseSignedClaims(token)
                        .getPayload()
                        .getExpiration();

        return expiration.before(
                new Date()
        );
    }


    // =================================================
    // CHIAVE FIRMA
    // =================================================

    private SecretKey getSigningKey() {

        return Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(
                        secretKey
                )
        );
    }
}