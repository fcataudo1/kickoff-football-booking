package com.fpc.football_booking.service;


import com.fpc.football_booking.dto.FootballFieldDto;
import com.fpc.football_booking.entity.FootballField;
import com.fpc.football_booking.exception.BusinessException;
import com.fpc.football_booking.exception.ConflictException;
import com.fpc.football_booking.exception.ResourceNotFoundException;
import com.fpc.football_booking.mapper.FootballFieldMapper;
import com.fpc.football_booking.repository.FootballFieldRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FootballFieldService
        extends AbstractService<FootballField, FootballFieldDto> {


    private final FootballFieldRepository footballFieldRepository;



    public FootballFieldService(
            FootballFieldRepository footballFieldRepository,
            FootballFieldMapper footballFieldMapper
    ) {

        super(
                footballFieldRepository,
                footballFieldMapper
        );

        this.footballFieldRepository = footballFieldRepository;

    }

    @Transactional
    public void disable(Long id) {


        FootballField field =
                repository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Football field not found"
                                )
                        );


        field.setActive(false);


        repository.save(field);

    }


    @Override
    @Transactional
    public FootballFieldDto insert(
            FootballFieldDto dto
    ) {


        if(footballFieldRepository.existsByName(dto.getName())) {

            throw new ConflictException(
                    "Football field already exists"
            );

        }


        FootballField field =
                new FootballField();


        field.setName(
                dto.getName()
        );


        field.setActive(true);



        FootballField saved =
                footballFieldRepository.save(field);



        return converter.toDTO(saved);

    }



    @Transactional(readOnly = true)
    public List<FootballFieldDto> getAvailableFields() {

        return converter.toDTOList(
                footballFieldRepository.findByActiveTrue()
        );

    }

    @Override
    @Transactional
    public FootballFieldDto update(
            FootballFieldDto dto
    ) {

        FootballField field =
                footballFieldRepository.findById(dto.getId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Football field not found"
                                )
                        );

        field.setName(dto.getName());

        FootballField updated =
                footballFieldRepository.save(field);

        return converter.toDTO(updated);
    }

}