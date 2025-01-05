package org.anasoid.instancio.petclinic.jpa.example.petclinic.generator.owner;

import org.anasoid.instancio.petclinic.jpa.example.petclinic.generator.AbstractEntityDataGenerator;
import org.instancio.InstancioApi;
import org.instancio.Model;
import org.springframework.samples.petclinic.owner.Visit;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Map;

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
public class VisitDataGenerator extends AbstractEntityDataGenerator<Visit> {
    @Override
    protected Map<String, Object> getFunctionalIdParams(Visit entity) {
        return Map.of();
    }

    @Override
    protected String getFunctionalIdQuery() {
        return null;
    }

    @Override
    protected Model<Visit> getEntityModel(InstancioApi<Visit> instancioApi) {
        return instancioApi
                .generate(field(Visit::getDate), gen -> gen.temporal().localDate()
                        .min(LocalDate.now().minusDays(365))
                        .max(LocalDate.now().minusDays(7)))
                .toModel();
    }

    @Override
    protected Class<Visit> getEntityClass() {
        return Visit.class;
    }
}
