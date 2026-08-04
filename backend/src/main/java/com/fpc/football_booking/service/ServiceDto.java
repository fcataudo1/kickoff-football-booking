package com.fpc.football_booking.service;

import java.util.List;

public interface ServiceDto<DTO> {


    List<DTO> getAll();


    DTO read(Long id);


    DTO insert(DTO dto);


    DTO update(DTO dto);


    void delete(Long id);

}
