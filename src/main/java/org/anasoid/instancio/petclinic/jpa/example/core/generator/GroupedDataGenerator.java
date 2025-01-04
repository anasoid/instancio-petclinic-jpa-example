package org.anasoid.instancio.petclinic.jpa.example.core.generator;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class GroupedDataGenerator implements DataGenerator {

    private final List<DataGenerator> dataGenerators;

    public GroupedDataGenerator(List<DataGenerator> dataGenerators) {
        this.dataGenerators = dataGenerators;
    }

    @Override
    public void generate() {
        for (DataGenerator dataGenerator : dataGenerators) {
            log.info(">> Start generate {} ", dataGenerator.getName());
            dataGenerator.generate();
            log.info(">> End generate {} ", dataGenerator.getName());
        }
    }

    @Override
    public String getName() {
        return "GroupedDataGenerator";
    }

    @Override
    public List generate(int min, int max) {
        throw new UnsupportedOperationException();
    }
}
