package com.fpc.football_booking.seeder;

import com.fpc.football_booking.entity.FootballField;
import com.fpc.football_booking.repository.FootballFieldRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;


@Component
@Order(2)
@Profile("dev")
public class FootballFieldSeeder
        extends AbstractSeeder<FootballField> {


    public FootballFieldSeeder(
            FootballFieldRepository repository
    ) {

        super(repository);

    }


    @Override
    protected int getNumberOfEntities() {

        return 3;

    }


    @Override
    protected FootballField createEntity(int index) {


        FootballField field = new FootballField();


        field.setName(
                "Campo " + index
        );


        field.setPricePerHour(
                new BigDecimal("50.00")
        );


        field.setActive(true);


        return field;

    }

}
