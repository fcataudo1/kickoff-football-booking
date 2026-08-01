package com.fpc.football_booking.service;

import com.fpc.football_booking.mapper.Converter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public abstract class AbstractService<ENTITY, DTO>
        implements ServiceDto<DTO> {


    protected final JpaRepository<ENTITY, Long> repository;

    protected final Converter<ENTITY, DTO> converter;


    protected AbstractService(
            JpaRepository<ENTITY, Long> repository,
            Converter<ENTITY, DTO> converter
    ) {
        this.repository = repository;
        this.converter = converter;
    }


    @Override
    public DTO insert(DTO dto) {

        ENTITY entity = converter.toEntity(dto);

        ENTITY savedEntity = repository.save(entity);

        return converter.toDTO(savedEntity);
    }


    @Override
    public List<DTO> getAll() {

        return converter.toDTOList(
                repository.findAll()
        );
    }


    @Override
    public DTO read(Long id) {

        ENTITY entity = repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Entity not found")
                );

        return converter.toDTO(entity);
    }


    @Override
    public DTO update(DTO dto) {

        ENTITY entity = converter.toEntity(dto);

        ENTITY updatedEntity = repository.save(entity);

        return converter.toDTO(updatedEntity);
    }


    @Override
    public void delete(Long id) {

        repository.deleteById(id);
    }

}
