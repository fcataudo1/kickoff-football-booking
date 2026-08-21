package com.fpc.football_booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fpc.football_booking.dto.FootballFieldDto;
import com.fpc.football_booking.exception.ConflictException;
import com.fpc.football_booking.exception.ResourceNotFoundException;
import com.fpc.football_booking.service.impl.FootballFieldService;
import com.fpc.football_booking.exception.GlobalExceptionHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class FootballFieldControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @Mock
    private FootballFieldService fieldService;

    private FootballFieldController fieldController;


    @BeforeEach
    void setUp() {

        fieldController =
                new FootballFieldController(
                        fieldService
                );

        mockMvc =
                MockMvcBuilders
                        .standaloneSetup(fieldController)
                        .setControllerAdvice(
                                new GlobalExceptionHandler()
                        )
                        .build();

        objectMapper =
                new ObjectMapper();
    }


    // =================================================
    // GET ALL
    // =================================================

    @Test
    void shouldReturnAllFootballFields()
            throws Exception {

        FootballFieldDto field1 =
                new FootballFieldDto();

        field1.setId(1L);
        field1.setName("Campo 1");
        field1.setActive(true);


        FootballFieldDto field2 =
                new FootballFieldDto();

        field2.setId(2L);
        field2.setName("Campo 2");
        field2.setActive(true);


        when(fieldService.getAll())
                .thenReturn(
                        List.of(field1, field2)
                );


        mockMvc.perform(
                        get("/api/fields")
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.length()")
                                .value(2)
                )
                .andExpect(
                        jsonPath("$[0].id")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$[0].name")
                                .value("Campo 1")
                )
                .andExpect(
                        jsonPath("$[0].active")
                                .value(true)
                )
                .andExpect(
                        jsonPath("$[1].id")
                                .value(2)
                )
                .andExpect(
                        jsonPath("$[1].name")
                                .value("Campo 2")
                );


        verify(fieldService)
                .getAll();
    }


    // =================================================
    // GET BY ID
    // =================================================

    @Test
    void shouldReturnFootballFieldById()
            throws Exception {

        FootballFieldDto field =
                new FootballFieldDto();

        field.setId(1L);
        field.setName("Campo 1");
        field.setActive(true);


        when(fieldService.read(1L))
                .thenReturn(field);


        mockMvc.perform(
                        get("/api/fields/1")
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.id")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.name")
                                .value("Campo 1")
                )
                .andExpect(
                        jsonPath("$.active")
                                .value(true)
                );


        verify(fieldService)
                .read(1L);
    }


    // =================================================
    // GET BY ID - NOT FOUND
    // =================================================

    @Test
    void shouldReturnNotFoundWhenFieldDoesNotExist()
            throws Exception {

        when(fieldService.read(99L))
                .thenThrow(
                        new ResourceNotFoundException(
                                "Football field not found"
                        )
                );


        mockMvc.perform(
                        get("/api/fields/99")
                )
                .andExpect(
                        status().isNotFound()
                )
                .andExpect(
                        jsonPath("$.message")
                                .value("Football field not found")
                );


        verify(fieldService)
                .read(99L);
    }


    // =================================================
    // CREATE
    // =================================================

    @Test
    void shouldCreateFootballField()
            throws Exception {

        FootballFieldDto request =
                new FootballFieldDto();

        request.setName("Campo Test");


        FootballFieldDto response =
                new FootballFieldDto();

        response.setId(1L);
        response.setName("Campo Test");
        response.setActive(true);


        when(fieldService.insert(any(FootballFieldDto.class)))
                .thenReturn(response);


        mockMvc.perform(
                        post("/api/fields")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(
                        status().isCreated()
                )
                .andExpect(
                        jsonPath("$.id")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.name")
                                .value("Campo Test")
                )
                .andExpect(
                        jsonPath("$.active")
                                .value(true)
                );


        verify(fieldService)
                .insert(any(FootballFieldDto.class));
    }


    // =================================================
    // CREATE - ALREADY EXISTS
    // =================================================

    @Test
    void shouldReturnConflictWhenFieldAlreadyExists()
            throws Exception {

        FootballFieldDto request =
                new FootballFieldDto();

        request.setName("Campo 1");


        when(
                fieldService.insert(
                        any(FootballFieldDto.class)
                )
        )
                .thenThrow(
                        new ConflictException(
                                "Football field already exists"
                        )
                );


        mockMvc.perform(
                        post("/api/fields")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(
                        status().isConflict()
                )
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Football field already exists"
                                )
                );


        verify(fieldService)
                .insert(any(FootballFieldDto.class));
    }


    // =================================================
    // UPDATE
    // =================================================

    @Test
    void shouldUpdateFootballField()
            throws Exception {

        FootballFieldDto request =
                new FootballFieldDto();

        request.setName("Campo 1 Nuovo");


        FootballFieldDto response =
                new FootballFieldDto();

        response.setId(1L);
        response.setName("Campo 1 Nuovo");
        response.setActive(true);


        when(
                fieldService.update(
                        any(FootballFieldDto.class)
                )
        )
                .thenReturn(response);


        mockMvc.perform(
                        put("/api/fields/1")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.id")
                                .value(1)
                )
                .andExpect(
                        jsonPath("$.name")
                                .value("Campo 1 Nuovo")
                )
                .andExpect(
                        jsonPath("$.active")
                                .value(true)
                );


        verify(fieldService)
                .update(
                        argThat(dto ->
                                dto.getId().equals(1L)
                        )
                );
    }


    // =================================================
    // UPDATE - NOT FOUND
    // =================================================

    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistingField()
            throws Exception {

        FootballFieldDto request =
                new FootballFieldDto();

        request.setName("Campo Nuovo");


        when(
                fieldService.update(
                        any(FootballFieldDto.class)
                )
        )
                .thenThrow(
                        new ResourceNotFoundException(
                                "Football field not found"
                        )
                );


        mockMvc.perform(
                        put("/api/fields/99")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        objectMapper.writeValueAsString(
                                                request
                                        )
                                )
                )
                .andExpect(
                        status().isNotFound()
                )
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Football field not found"
                                )
                );


        verify(fieldService)
                .update(
                        argThat(dto ->
                                dto.getId().equals(99L)
                        )
                );
    }


    // =================================================
    // DISABLE
    // =================================================

    @Test
    void shouldDisableFootballField()
            throws Exception {

        doNothing()
                .when(fieldService)
                .disable(1L);


        mockMvc.perform(
                        put("/api/fields/1/disable")
                )
                .andExpect(
                        status().isNoContent()
                );


        verify(fieldService)
                .disable(1L);
    }


    // =================================================
    // DISABLE - NOT FOUND
    // =================================================

    @Test
    void shouldReturnNotFoundWhenDisablingNonExistingField()
            throws Exception {

        doThrow(
                new ResourceNotFoundException(
                        "Football field not found"
                )
        )
                .when(fieldService)
                .disable(99L);


        mockMvc.perform(
                        put("/api/fields/99/disable")
                )
                .andExpect(
                        status().isNotFound()
                )
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Football field not found"
                                )
                );


        verify(fieldService)
                .disable(99L);
    }
}