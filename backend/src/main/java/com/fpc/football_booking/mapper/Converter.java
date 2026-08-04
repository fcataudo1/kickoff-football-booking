package com.fpc.football_booking.mapper;

import java.util.List;

public interface Converter<Entity, DTO> {

    Entity toEntity(DTO dto);

    DTO toDTO(Entity entity);

    List<DTO> toDTOList(Iterable<Entity> entityList);

    List<Entity> toEntityList(Iterable<DTO> entityList);

}