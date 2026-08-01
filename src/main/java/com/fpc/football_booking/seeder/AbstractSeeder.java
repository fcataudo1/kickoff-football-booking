package com.fpc.football_booking.seeder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;
import java.util.List;
public abstract class AbstractSeeder<E> implements CommandLineRunner {


    protected final JpaRepository<E, Long> repository;

    protected final Logger logger =
            LoggerFactory.getLogger(getClass());


    protected AbstractSeeder(
            JpaRepository<E, Long> repository
    ) {

        this.repository = repository;

    }


    @Override
    public void run(String... args) {


        if(repository.count() == 0) {


            List<E> entities = new ArrayList<>();


            for(int i = 1; i <= getNumberOfEntities(); i++) {

                entities.add(createEntity(i));

            }


            repository.saveAll(entities);


            logger.info(
                    "{} initialized: {} records created",
                    getClass().getSimpleName(),
                    entities.size()
            );


        } else {


            logger.info(
                    "{} skipped: database already contains data",
                    getClass().getSimpleName()
            );


        }

    }


    protected int getNumberOfEntities() {

        return 5;

    }


    protected abstract E createEntity(int index);

}