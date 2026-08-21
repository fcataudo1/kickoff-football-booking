package com.fpc.football_booking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.http.HttpMethod;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter
    ) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {

        return new BCryptPasswordEncoder();

    }


    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http
    ) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .cors(cors -> {})

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )


                // =========================
                // EXCEPTION HANDLING
                // =========================

                .exceptionHandling(exception -> exception

                        // 401 - non autenticato
                        .authenticationEntryPoint(
                                (request, response, authException) -> {

                                    response.setStatus(
                                            HttpServletResponse.SC_UNAUTHORIZED
                                    );

                                    response.setContentType(
                                            "application/json"
                                    );

                                    response.getWriter().write(
                                            "{\"message\":\"Unauthorized\"}"
                                    );
                                }
                        )

                        // 403 - autenticato ma senza permessi
                        .accessDeniedHandler(
                                (request, response, accessDeniedException) -> {

                                    response.setStatus(
                                            HttpServletResponse.SC_FORBIDDEN
                                    );

                                    response.setContentType(
                                            "application/json"
                                    );

                                    response.getWriter().write(
                                            "{\"message\":\"Forbidden\"}"
                                    );
                                }
                        )
                )


                // =========================
                // AUTHORIZATION
                // =========================

                .authorizeHttpRequests(auth -> auth


                        // =========================
                        // PUBLIC
                        // =========================

                        .requestMatchers(
                                "/api/auth/login",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        )
                        .permitAll()


                        // =========================
                        // USERS
                        // =========================

                        // Registrazione pubblica
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/users"
                        )
                        .permitAll()

                        // Gestione utenti → solo ADMIN
                        .requestMatchers(
                                "/api/users/**"
                        )
                        .hasRole("ADMIN")


                        // =========================
                        // AUTH
                        // =========================

                        .requestMatchers(
                                "/api/auth/me"
                        )
                        .authenticated()


                        // =========================
                        // RESERVATIONS
                        // =========================

                        // Creazione prenotazione
                        // → SOLO CLIENTE
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/reservations"
                        )
                        .hasRole("CLIENTE")


                        // Proprie prenotazioni
                        // → SOLO CLIENTE
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/reservations/my"
                        )
                        .hasRole("CLIENTE")


                        // Tutte le prenotazioni
                        // → SOLO RECEPTIONIST
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/reservations"
                        )
                        .hasRole("RECEPTIONIST")


                        // Prenotazioni di un campo
                        // → SOLO RECEPTIONIST
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/reservations/field/**"
                        )
                        .hasRole("RECEPTIONIST")


                        // Cancellazione propria prenotazione
                        // → SOLO CLIENTE
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/reservations/**"
                        )
                        .hasRole("CLIENTE")


                        // Annullamento prenotazione cliente
                        // → SOLO RECEPTIONIST
                        .requestMatchers(
                                HttpMethod.PATCH,
                                "/api/reservations/*/cancel"
                        )
                        .hasRole("RECEPTIONIST")


                        // =========================
                        // FIELDS
                        // =========================

                        // Lettura campi
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/fields",
                                "/api/fields/**"
                        )
                        .authenticated()

                        // Creazione/modifica/disabilitazione
                        // → SOLO ADMIN
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/fields"
                        )
                        .hasRole("ADMIN")

                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/fields/**"
                        )
                        .hasRole("ADMIN")


                        // =========================
                        // EVERYTHING ELSE
                        // =========================

                        .anyRequest()
                        .authenticated()
                )


                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );


        return http.build();
    }


    // =========================
    // CORS
    // =========================

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();


        configuration.setAllowedOrigins(
                List.of(
                        "http://localhost:4200"
                )
        );


        configuration.setAllowedMethods(
                List.of(
                        "GET",
                        "POST",
                        "PUT",
                        "PATCH",
                        "DELETE",
                        "OPTIONS"
                )
        );


        configuration.setAllowedHeaders(
                List.of("*")
        );


        configuration.setAllowCredentials(true);


        UrlBasedCorsConfigurationSource source =
                new UrlBasedCorsConfigurationSource();


        source.registerCorsConfiguration(
                "/**",
                configuration
        );


        return source;
    }
}