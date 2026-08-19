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

                .exceptionHandling(exception -> exception

                        // 401 - utente NON autenticato
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

                        // 403 - utente autenticato
                        // ma senza permessi
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

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(
                                "/api/auth/login",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()


                                // USERS

                                // Registrazione pubblica
                                .requestMatchers(
                                        HttpMethod.POST,
                                        "/api/users"
                                )
                                .permitAll()

                                // Gestione utenti
                                .requestMatchers("/api/users/**")
                                .hasRole("ADMIN")


                        // =========================
                        // AUTH
                        // =========================

                        .requestMatchers("/api/auth/me")
                        .authenticated()


                        // =========================
                        // RESERVATIONS
                        // =========================

                        // Creazione prenotazione
                        .requestMatchers(HttpMethod.POST, "/api/reservations")
                        .hasAnyRole(
                                "CLIENTE"
                        )


                        // Proprie prenotazioni
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/reservations/my"
                        )
                        .hasAnyRole(
                                "CLIENTE",
                                "RECEPTIONIST",
                                "ADMIN"
                        )


                        // Tutte le prenotazioni
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/reservations"
                        )
                        .hasAnyRole(
                                "RECEPTIONIST",
                                "ADMIN"
                        )


                        // Prenotazioni di un campo
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/reservations/field/**"
                        )
                        .hasAnyRole(
                                "RECEPTIONIST",
                                "ADMIN"
                        )


                        // Cancellazione
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/reservations/**"
                        )
                        .hasAnyRole(
                                "CLIENTE"
                        )


                                // =========================
                                // ANNULLAMENTO RECEPTIONIST
                                // =========================

                                .requestMatchers(
                                        HttpMethod.PATCH,
                                        "/api/reservations/*/cancel"
                                )
                                .hasAnyRole(
                                        "RECEPTIONIST",
                                        "ADMIN"
                                )


                        // Tutto il resto
                        .anyRequest()
                        .authenticated()
                )

                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration configuration =
                new CorsConfiguration();

        configuration.setAllowedOrigins(
                List.of("http://localhost:4200")
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