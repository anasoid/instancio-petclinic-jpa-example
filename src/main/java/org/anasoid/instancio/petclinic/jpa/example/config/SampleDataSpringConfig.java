package org.anasoid.instancio.petclinic.jpa.example.config;


import org.anasoid.instancio.petclinic.jpa.example.core.generator.DataGenerator;
import org.anasoid.instancio.petclinic.jpa.example.core.generator.GroupedDataGenerator;
import org.anasoid.instancio.petclinic.jpa.example.petclinic.generator.owner.PetTypeDataGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class SampleDataSpringConfig {

    @Bean
    org.anasoid.instancio.petclinic.jpa.example.core.generator.GroupedDataGenerator mainDataGenerator(
            PetTypeDataGenerator petTypeDataGenerator) {
        List<DataGenerator> generators =
                List.of(petTypeDataGenerator);
        return new GroupedDataGenerator(generators);
    }
}
