package org.anasoid.instancio.petclinic.jpa.example;


import lombok.extern.slf4j.Slf4j;
import org.anasoid.instancio.petclinic.jpa.example.core.generator.GroupedDataGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication(scanBasePackages = {"org.anasoid.instancio.petclinic.jpa.example", ""})
@Slf4j
@EntityScan("org.springframework.samples.petclinic")
public class InstancioPetclinicJpaExampleApplication implements CommandLineRunner {

    @Autowired()
    @Qualifier("mainDataGenerator")
    private GroupedDataGenerator mainDataGenerator;

    public static void main(String[] args) {
        try {
            log.info("STARTING THE APPLICATION");
            System.setProperty("spring.config.name", "application-config");
            SpringApplication.run(InstancioPetclinicJpaExampleApplication.class, args);
            log.info("APPLICATION FINISHED");
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
