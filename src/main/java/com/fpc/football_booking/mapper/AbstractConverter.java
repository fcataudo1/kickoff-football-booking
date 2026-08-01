package com.fpc.football_booking.mapper;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractConverter<Entity, DTO> implements Converter<Entity, DTO> {


    @Override
    public List<Entity> toEntityList(Iterable<DTO> dtos) {

        List<Entity> entities = new ArrayList<>();

        if (dtos != null) {
            for (DTO dto : dtos) {
                entities.add(toEntity(dto));
            }
        }

        return entities;
    }


    @Override
    public List<DTO> toDTOList(Iterable<Entity> entities) {

        List<DTO> dtos = new ArrayList<>();

        if (entities != null) {
            for (Entity entity : entities) {
                dtos.add(toDTO(entity));
            }
        }

        return dtos;
    }
}
