package com.fpc.football_booking.service;

import com.fpc.football_booking.dto.FootballFieldDto;
import com.fpc.football_booking.entity.FootballField;
import com.fpc.football_booking.exception.ConflictException;
import com.fpc.football_booking.exception.ResourceNotFoundException;
import com.fpc.football_booking.mapper.FootballFieldMapper;
import com.fpc.football_booking.repository.FootballFieldRepository;
import com.fpc.football_booking.service.impl.FootballFieldService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FootballFieldServiceTest {

    @Mock
    private FootballFieldRepository footballFieldRepository;

    @Mock
    private FootballFieldMapper footballFieldMapper;

    @InjectMocks
    private FootballFieldService footballFieldService;


    @Test
    void shouldCreateFootballField() {

        FootballFieldDto dto = new FootballFieldDto();
        dto.setName("Campo Test");

        FootballField savedField = new FootballField();
        savedField.setId(1L);
        savedField.setName("Campo Test");
        savedField.setActive(true);

        FootballFieldDto savedDto = new FootballFieldDto();
        savedDto.setId(1L);
        savedDto.setName("Campo Test");
        savedDto.setActive(true);

        when(footballFieldRepository.existsByName("Campo Test"))
                .thenReturn(false);

        when(footballFieldRepository.save(any(FootballField.class)))
                .thenReturn(savedField);

        when(footballFieldMapper.toDTO(savedField))
                .thenReturn(savedDto);

        FootballFieldDto result =
                footballFieldService.insert(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Campo Test", result.getName());
        assertTrue(result.isActive());

        verify(footballFieldRepository)
                .existsByName("Campo Test");

        verify(footballFieldRepository)
                .save(any(FootballField.class));
    }


    @Test
    void shouldThrowExceptionWhenFieldAlreadyExists() {

        FootballFieldDto dto = new FootballFieldDto();
        dto.setName("Campo 1");

        when(footballFieldRepository.existsByName("Campo 1"))
                .thenReturn(true);

        ConflictException exception = assertThrows(
                ConflictException.class,
                () -> footballFieldService.insert(dto)
        );

        assertEquals(
                "Football field already exists",
                exception.getMessage()
        );

        verify(footballFieldRepository)
                .existsByName("Campo 1");

        verify(footballFieldRepository, never())
                .save(any(FootballField.class));
    }


    @Test
    void shouldDisableFootballField() {

        FootballField field = new FootballField();
        field.setId(1L);
        field.setName("Campo 1");
        field.setActive(true);

        when(footballFieldRepository.findById(1L))
                .thenReturn(Optional.of(field));

        footballFieldService.disable(1L);

        assertFalse(field.isActive());

        verify(footballFieldRepository)
                .findById(1L);

        verify(footballFieldRepository)
                .save(field);
    }

    @Test
    void shouldThrowExceptionWhenDisablingNonExistingField() {

        when(footballFieldRepository.findById(99L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> footballFieldService.disable(99L)
        );

        assertEquals(
                "Football field not found",
                exception.getMessage()
        );

        verify(footballFieldRepository)
                .findById(99L);

        verify(footballFieldRepository, never())
                .save(any(FootballField.class));
    }

    @Test
    void shouldUpdateFootballField() {

        FootballFieldDto dto = new FootballFieldDto();
        dto.setId(1L);
        dto.setName("Campo 1 Nuovo");

        FootballField field = new FootballField();
        field.setId(1L);
        field.setName("Campo 1");
        field.setActive(true);

        FootballFieldDto updatedDto = new FootballFieldDto();
        updatedDto.setId(1L);
        updatedDto.setName("Campo 1 Nuovo");
        updatedDto.setActive(true);

        when(footballFieldRepository.findById(1L))
                .thenReturn(Optional.of(field));

        when(footballFieldRepository.save(field))
                .thenReturn(field);

        when(footballFieldMapper.toDTO(field))
                .thenReturn(updatedDto);

        FootballFieldDto result =
                footballFieldService.update(dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Campo 1 Nuovo", result.getName());
        assertTrue(result.isActive());

        verify(footballFieldRepository)
                .findById(1L);

        verify(footballFieldRepository)
                .save(field);
    }

    @Test
    void shouldThrowExceptionWhenUpdatingNonExistingField() {

        FootballFieldDto dto = new FootballFieldDto();

        dto.setId(99L);
        dto.setName("Campo Nuovo");


        when(
                footballFieldRepository.findById(99L)
        ).thenReturn(Optional.empty());


        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> footballFieldService.update(dto)
        );


        assertEquals(
                "Football field not found",
                exception.getMessage()
        );


        verify(footballFieldRepository)
                .findById(99L);


        verify(footballFieldRepository, never())
                .save(any(FootballField.class));
    }
}