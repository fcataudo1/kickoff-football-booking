package com.fpc.football_booking.mapper;


import com.fpc.football_booking.dto.ReservationDto;
import com.fpc.football_booking.entity.Reservation;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper
        extends AbstractConverter<Reservation, ReservationDto> {


    @Override
    public ReservationDto toDTO(Reservation entity) {

        if (entity == null) {
            return null;
        }


        ReservationDto dto = new ReservationDto();


        dto.setId(entity.getId());


        dto.setCustomerName(
                entity.getCustomerName()
        );


        dto.setCustomerPhone(
                entity.getCustomerPhone()
        );


        dto.setCustomerEmail(
                entity.getCustomerEmail()
        );


        if (entity.getFootballField() != null) {

            dto.setFootballFieldId(
                    entity.getFootballField().getId()
            );

        }


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


        return dto;

    }



    @Override
    public Reservation toEntity(ReservationDto dto) {


        if (dto == null) {
            return null;
        }


        Reservation entity = new Reservation();


        entity.setId(
                dto.getId()
        );


        entity.setCustomerName(
                dto.getCustomerName()
        );


        entity.setCustomerPhone(
                dto.getCustomerPhone()
        );


        entity.setCustomerEmail(
                dto.getCustomerEmail()
        );


        entity.setReservationDate(
                dto.getReservationDate()
        );


        entity.setStartTime(
                dto.getStartTime()
        );


        entity.setStatus(
                dto.getStatus()
        );


        entity.setPrice(
                dto.getPrice()
        );


        return entity;

    }

}