package org.anasoid.instancio.petclinic.jpa.example.config;


import org.anasoid.instancio.petclinic.jpa.example.core.generator.DataGenerator;
import org.anasoid.instancio.petclinic.jpa.example.core.generator.GroupedDataGenerator;
import org.anasoid.instancio.petclinic.jpa.example.petclinic.generator.owner.OwnerDataGenerator;
import org.anasoid.instancio.petclinic.jpa.example.petclinic.generator.owner.PetTypeDataGenerator;
import org.anasoid.instancio.petclinic.jpa.example.petclinic.generator.vet.SpecialtyDataGenerator;
import org.anasoid.instancio.petclinic.jpa.example.petclinic.generator.vet.VetDataGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SampleDataSpringConfig {

    @Bean
    GroupedDataGenerator mainDataGenerator(
            PetTypeDataGenerator petTypeDataGenerator, SpecialtyDataGenerator specialtyDataGenerator, VetDataGenerator vetDataGenerator, OwnerDataGenerator ownerDataGenerator) {
        List<DataGenerator> generators =
                List.of(petTypeDataGenerator, specialtyDataGenerator, vetDataGenerator, ownerDataGenerator);
        return new GroupedDataGenerator(generators);
    }
}
