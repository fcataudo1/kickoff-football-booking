package com.fpc.football_booking.entity;

import com.fpc.football_booking.entity.enums.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "app_users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String firstName;


    @Column(nullable = false)
    private String lastName;


    @Column(nullable = false, unique = true)
    private String email;


    @Column(nullable = false)
    private String password;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

}