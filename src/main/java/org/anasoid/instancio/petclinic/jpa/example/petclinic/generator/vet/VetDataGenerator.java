package org.anasoid.instancio.petclinic.jpa.example.petclinic.generator.vet;

import org.anasoid.instancio.petclinic.jpa.example.petclinic.generator.model.PersonDataGenerator;
import org.instancio.InstancioApi;
import org.instancio.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.vet.Vet;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

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
public class VetDataGenerator extends PersonDataGenerator<Vet> {
    @Autowired
    private SpecialtyDataGenerator specialtyDataGenerator;

    @Override
    protected Model<Vet> getEntityModel(InstancioApi<Vet> instancioApi) {
        return instancioApi
                .supply(field(Vet::getSpecialties), () -> new HashSet<>( specialtyDataGenerator.getRandomFromDatabase(1, 2)))
                .toModel();
    }

    @Override
    protected Class<Vet> getEntityClass() {
        return Vet.class;
    }
}
