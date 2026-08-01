package com.fpc.football_booking.controller;


import com.fpc.football_booking.dto.AppUserDto;
import com.fpc.football_booking.service.AppUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class AppUserController {


    private final AppUserService userService;


    public AppUserController(
            AppUserService userService
    ) {

        this.userService = userService;

    }


    @GetMapping
    public List<AppUserDto> getAll() {

        return userService.getAll();

    }



    @GetMapping("/{id}")
    public AppUserDto getById(
            @PathVariable Long id
    ) {

        return userService.read(id);

    }



    @PostMapping
    public ResponseEntity<AppUserDto> create(
            @RequestBody AppUserDto dto
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        userService.insert(dto)
                );

    }



    @PutMapping("/{id}")
    public AppUserDto update(
            @PathVariable Long id,
            @RequestBody AppUserDto dto
    ) {

        dto.setId(id);

        return userService.update(dto);

    }



    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(
            @PathVariable Long id
    ) {

        userService.delete(id);

        return ResponseEntity
                .noContent()
                .build();

    }

}