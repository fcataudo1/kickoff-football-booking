package com.fpc.football_booking.controller;

import com.fpc.football_booking.dto.FootballFieldDto;
import com.fpc.football_booking.service.FootballFieldService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/fields")
public class FootballFieldController {


    private final FootballFieldService fieldService;


    public FootballFieldController(
            FootballFieldService fieldService
    ) {

        this.fieldService = fieldService;

    }



    @GetMapping
    public List<FootballFieldDto> getAll(){

        return fieldService.getAll();

    }



    @GetMapping("/{id}")
    public FootballFieldDto getById(
            @PathVariable Long id
    ){

        return fieldService.read(id);

    }



    @PostMapping
    public ResponseEntity<FootballFieldDto> create(
            @RequestBody FootballFieldDto dto
    ){

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        fieldService.insert(dto)
                );

    }



    @PutMapping("/{id}")
    public FootballFieldDto update(
            @PathVariable Long id,
            @RequestBody FootballFieldDto dto
    ){

        dto.setId(id);

        return fieldService.update(dto);

    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ){

        fieldService.delete(id);

        return ResponseEntity
                .noContent()
                .build();

    }

}