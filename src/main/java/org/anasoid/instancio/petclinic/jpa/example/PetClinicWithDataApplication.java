package org.anasoid.instancio.petclinic.jpa.example;


import lombok.extern.slf4j.Slf4j;
import org.anasoid.instancio.petclinic.jpa.example.core.generator.GroupedDataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"org.anasoid.instancio.petclinic.jpa.example.config",
        "org.anasoid.instancio.petclinic.jpa.example.core",
        "org.anasoid.instancio.petclinic.jpa.example.petclinic", "org.springframework.samples"})
@Slf4j
public class PetClinicWithDataApplication implements CommandLineRunner {

    @Autowired()
    @Qualifier("mainDataGenerator")
    private GroupedDataGenerator mainDataGenerator;

    public static void main(String[] args) {
        try {
            log.info("STARTING load Data");
            System.setProperty("spring.config.name", "application-config");
            SpringApplication.run(PetClinicWithDataApplication.class, args);
            log.info("End load Data");
        } catch (Exception ex) {
            ex.printStackTrace();
            throw ex;
        }
    }

    @Override
    public void run(String... args) {
        mainDataGenerator.generate();
    }

}
