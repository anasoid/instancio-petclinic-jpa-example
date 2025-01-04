package org.anasoid.instancio.petclinic.jpa.example.petclinic.generator.owner;


import org.anasoid.instancio.petclinic.jpa.example.petclinic.generator.model.NamedEntityDataGenerator;
import org.instancio.InstancioApi;
import org.instancio.Model;
import org.springframework.samples.petclinic.owner.PetType;
import org.springframework.stereotype.Component;

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
 * Date :   1/4/25
 *
 */
@Component
public class PetTypeDataGenerator extends NamedEntityDataGenerator<PetType> {


    @Override
    protected Model<PetType> getEntityModel(InstancioApi<PetType> instancioApi) {
        return instancioApi.toModel();
    }

    @Override
    protected Class<PetType> getEntityClass() {
        return PetType.class;
    }


}
