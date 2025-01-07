package org.anasoid.instancio.petclinic.jpa.example;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.samples.petclinic.owner.Owner;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.samples.petclinic.owner.Visit;
import org.springframework.samples.petclinic.vet.Specialty;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.test.context.ContextConfiguration;

import java.text.MessageFormat;
import java.util.List;


@SpringBootTest()
@ContextConfiguration(classes = InstancioPetClinicJpaExampleApplication.class)
class InstancioPetClinicJpaExampleApplicationTests {

    @Autowired
    EntityManager entityManager;

    @BeforeAll
    static void init() {
        System.setProperty("spring.config.name", "application-config");
    }


    @Test
    void contextLoads() {
        List.of(Vet.class, Owner.class, Pet.class, Visit.class, Specialty.class, PetType.class)
                .forEach(c -> Assertions
                        .assertTrue(getCountFromDatabase(Vet.class) > 0));

    }


    public long getCountFromDatabase(Class<?> clazz) {
        String finalQuery =
                MessageFormat.format("select count(*) from {0}", clazz.getSimpleName());
        List<Long> resultsDatabase = entityManager.createQuery(finalQuery).getResultList();

        return resultsDatabase.get(0);
    }

}
