package com.fpc.football_booking.mapper;

import com.fpc.football_booking.dto.AppUserDto;
import com.fpc.football_booking.entity.AppUser;
import org.springframework.stereotype.Component;

@Component
public class AppUserMapper extends AbstractConverter<AppUser, AppUserDto> {


    @Override
    public AppUserDto toDTO(AppUser entity) {

        if (entity == null) {
            return null;
        }

        AppUserDto dto = new AppUserDto();

        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setRole(entity.getRole());

        return dto;
    }


    @Override
    public AppUser toEntity(AppUserDto dto) {

        if (dto == null) {
            return null;
        }

        AppUser entity = new AppUser();

        entity.setId(dto.getId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setRole(dto.getRole());

        return entity;
    }
}
