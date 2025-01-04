package org.anasoid.instancio.petclinic.jpa.example.core.generator;

import java.util.List;

public interface DataGenerator {

    void generate();

    String getName();

    List generate(int min, int max);
}
