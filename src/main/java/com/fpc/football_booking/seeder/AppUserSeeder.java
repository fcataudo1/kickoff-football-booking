package com.fpc.football_booking.seeder;

import com.fpc.football_booking.entity.AppUser;
import com.fpc.football_booking.entity.enums.Role;
import com.fpc.football_booking.repository.AppUserRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


@Component
@Order(1)
@Profile("dev")
public class AppUserSeeder
        extends AbstractSeeder<AppUser> {


    public AppUserSeeder(
            AppUserRepository repository
    ) {

        super(repository);

    }


    @Override
    protected int getNumberOfEntities() {

        return 5;

    }


    @Override
    protected AppUser createEntity(int index) {


        AppUser user = new AppUser();


        user.setFirstName(
                "User" + index
        );


        user.setLastName(
                "Test"
        );


        user.setEmail(
                "user" + index + "@example.com"
        );


        user.setPassword(
                "password"
        );


        if(index == 1){

            user.setRole(Role.ADMIN);

        } else {

            user.setRole(Role.CUSTOMER);

        }


        return user;

    }

}