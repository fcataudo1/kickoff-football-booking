package com.fpc.football_booking.controller;

import com.fpc.football_booking.dto.FootballFieldDto;
import com.fpc.football_booking.service.FootballFieldService;
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
    public FootballFieldDto create(
            @RequestBody FootballFieldDto dto
    ){

        return fieldService.insert(dto);

    }


    @PutMapping
    public FootballFieldDto update(
            @RequestBody FootballFieldDto dto
    ){

        return fieldService.update(dto);

    }


    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id
    ){

        fieldService.delete(id);

    }

}