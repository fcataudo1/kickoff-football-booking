package com.fpc.football_booking.mapper;


import com.fpc.football_booking.dto.ReservationDto;
import com.fpc.football_booking.entity.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper
        extends AbstractConverter<Reservation, ReservationDto> {

    private final FootballFieldMapper footballFieldMapper;
    private final UserMapper userMapper;

    public ReservationMapper(
            FootballFieldMapper footballFieldMapper,
            UserMapper userMapper
    ) {
        this.footballFieldMapper = footballFieldMapper;
        this.userMapper = userMapper;
    }

    @Override
    public ReservationDto toDTO(Reservation entity) {

        if (entity == null) {
            return null;
        }

        ReservationDto dto = new ReservationDto();

        dto.setId(entity.getId());

        dto.setReservationDate(
                entity.getReservationDate()
        );

        dto.setStartTime(
                entity.getStartTime()
        );

        dto.setStatus(
                entity.getStatus()
        );

        dto.setPrice(
                entity.getPrice()
        );

        dto.setFootballField(
                footballFieldMapper.toDTO(
                        entity.getFootballField()
                )
        );

        dto.setUser(
                userMapper.toResponseDto(
                        entity.getUser()
                )
        );

        return dto;
    }

    @Override
    public Reservation toEntity(ReservationDto dto) {

        if (dto == null) {
            return null;
        }

        Reservation entity = new Reservation();

        entity.setReservationDate(
                dto.getReservationDate()
        );

        entity.setStartTime(
                dto.getStartTime()
        );

        return entity;
    }
}