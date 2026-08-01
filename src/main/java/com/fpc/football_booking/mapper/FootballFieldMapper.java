package com.fpc.football_booking.mapper;


import com.fpc.football_booking.dto.FootballFieldDto;
import com.fpc.football_booking.entity.FootballField;
import org.springframework.stereotype.Component;

@Component
public class FootballFieldMapper extends AbstractConverter<FootballField, FootballFieldDto> {


    @Override
    public FootballFieldDto toDTO(FootballField entity) {

        if (entity == null) {
            return null;
        }

        FootballFieldDto dto = new FootballFieldDto();

        dto.setId(entity.getId());
        dto.setName(entity.getName());
        dto.setPricePerHour(entity.getPricePerHour());
        dto.setActive(entity.isActive());

        return dto;
    }


    @Override
    public FootballField toEntity(FootballFieldDto dto) {

        if (dto == null) {
            return null;
        }

        FootballField entity = new FootballField();

        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setPricePerHour(dto.getPricePerHour());
        entity.setActive(dto.isActive());

        return entity;
    }
}
