package com.fpc.football_booking.service;


import com.fpc.football_booking.dto.FootballFieldDto;
import com.fpc.football_booking.entity.FootballField;
import com.fpc.football_booking.mapper.FootballFieldMapper;
import com.fpc.football_booking.repository.FootballFieldRepository;
import org.springframework.stereotype.Service;

@Service
public class FootballFieldService
        extends AbstractService<FootballField, FootballFieldDto> {


    public FootballFieldService(
            FootballFieldRepository footballFieldRepository,
            FootballFieldMapper footballFieldMapper
    ) {

        super(
                footballFieldRepository,
                footballFieldMapper
        );

    }

}
