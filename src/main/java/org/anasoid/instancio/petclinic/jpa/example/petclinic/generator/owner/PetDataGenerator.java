package org.anasoid.instancio.petclinic.jpa.example.petclinic.generator.owner;

import org.anasoid.instancio.petclinic.jpa.example.petclinic.generator.model.NamedEntityDataGenerator;
import org.instancio.InstancioApi;
import org.instancio.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.owner.Pet;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;

import static org.instancio.Select.field;

/*
 * Copyright 2023-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * @author : anasoid
 * Date :   1/5/25
 */
@Component
public class PetDataGenerator extends NamedEntityDataGenerator<Pet> {
    @Autowired
    private VisitDataGenerator visitDataGenerator;
    @Autowired
    private PetTypeDataGenerator petTypeDataGenerator;

    @Override
    protected Model<Pet> getEntityModel(InstancioApi<Pet> instancioApi) {
        return instancioApi
                .supply(field(Pet::getVisits), () -> new HashSet<>(visitDataGenerator.generate(0, 5)))
                .supply(field(Pet::getType), () -> petTypeDataGenerator.getRandomFromDatabase())
                .generate(field(Pet::getBirthDate), gen -> gen.temporal().localDate()
                        .min(LocalDate.now().minusYears(10))
                        .max(LocalDate.now().minusDays(7)))
                .toModel();
    }

    @Override
    protected Class<Pet> getEntityClass() {
        return Pet.class;
    }

    @Override
    protected String getFunctionalIdQuery() {
        return null;
    }

}
