package com.fpc.football_booking.repository;


import com.fpc.football_booking.entity.FootballField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FootballFieldRepository
        extends JpaRepository<FootballField, Long> {


    List<FootballField> findByActiveTrue();
    Optional<FootballField> findByName(String name);

}
