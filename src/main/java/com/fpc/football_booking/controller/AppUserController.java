package com.fpc.football_booking.controller;


import com.fpc.football_booking.dto.AppUserDto;
import com.fpc.football_booking.service.AppUserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class AppUserController {


    private final AppUserService userService;


    public AppUserController(AppUserService userService) {
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
    public AppUserDto create(
            @RequestBody AppUserDto dto
    ) {

        return userService.insert(dto);

    }


    @PutMapping
    public AppUserDto update(
            @RequestBody AppUserDto dto
    ) {

        return userService.update(dto);

    }


    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable Long id
    ) {

        userService.delete(id);

    }

}
