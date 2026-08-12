package com.fpc.football_booking.controller;


import com.fpc.football_booking.dto.FootballFieldDto;
import com.fpc.football_booking.exception.ConflictException;
import com.fpc.football_booking.exception.ResourceNotFoundException;
import com.fpc.football_booking.service.FootballFieldService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


@WebMvcTest(FootballFieldController.class)
class FootballFieldControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private FootballFieldService fieldService;


    @Test
    void shouldGetAllFields() throws Exception {

        FootballFieldDto field = new FootballFieldDto();

        field.setId(1L);
        field.setName("Campo 1");
        field.setActive(true);


        when(fieldService.getAll())
                .thenReturn(List.of(field));


        mockMvc.perform(
                        get("/api/fields")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Campo 1"))
                .andExpect(jsonPath("$[0].active").value(true));
    }

    @Test
    void shouldGetFieldById() throws Exception {

        FootballFieldDto field = new FootballFieldDto();

        field.setId(1L);
        field.setName("Campo 1");
        field.setActive(true);


        when(fieldService.read(1L))
                .thenReturn(field);


        mockMvc.perform(
                        get("/api/fields/1")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Campo 1"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void shouldCreateFootballField() throws Exception {

        FootballFieldDto dto = new FootballFieldDto();

        dto.setId(1L);
        dto.setName("Campo Nuovo");
        dto.setActive(true);


        when(fieldService.insert(any(FootballFieldDto.class)))
                .thenReturn(dto);


        String json = """
        {
            "name": "Campo Nuovo",
            "active": true
        }
        """;


        mockMvc.perform(
                        post("/api/fields")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Campo Nuovo"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void shouldUpdateFootballField() throws Exception {

        FootballFieldDto dto = new FootballFieldDto();

        dto.setId(1L);
        dto.setName("Campo 1 Aggiornato");
        dto.setActive(true);


        when(fieldService.update(any(FootballFieldDto.class)))
                .thenReturn(dto);


        String json = """
            {
                "id": 1,
                "name": "Campo 1 Aggiornato",
                "active": true
            }
            """;


        mockMvc.perform(
                        put("/api/fields/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Campo 1 Aggiornato"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @Test
    void shouldDisableFootballField() throws Exception {

        doNothing()
                .when(fieldService)
                .disable(1L);


        mockMvc.perform(
                        put("/api/fields/1/disable")
                )
                .andExpect(status().isNoContent());


        verify(fieldService)
                .disable(1L);
    }

    @Test
    void shouldReturnConflictWhenFootballFieldAlreadyExists() throws Exception {

        when(fieldService.insert(any(FootballFieldDto.class)))
                .thenThrow(
                        new ConflictException(
                                "Football field already exists"
                        )
                );

        String json = """
        {
            "name": "Campo 1",
            "active": true
        }
        """;

        mockMvc.perform(
                        post("/api/fields")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(json)
                )
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("Football field already exists"));
    }

    @Test
    void shouldReturnNotFoundWhenFootballFieldDoesNotExist() throws Exception {

        when(fieldService.read(99L))
                .thenThrow(
                        new ResourceNotFoundException(
                                "Football field not found"
                        )
                );

        mockMvc.perform(
                        get("/api/fields/99")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Football field not found"));
    }
}
