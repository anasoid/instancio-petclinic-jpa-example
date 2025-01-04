package org.anasoid.instancio.petclinic.jpa.example.petclinic.generator;

import org.anasoid.instancio.petclinic.jpa.example.core.generator.AbstractDataGenerator;
import org.instancio.InstancioApi;
import org.springframework.samples.petclinic.model.BaseEntity;

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
 * Date :   1/4/25
 */public abstract class AbstractEntityDataGenerator<T extends BaseEntity> extends AbstractDataGenerator<T, Integer> {
    @Override
    protected Integer getId(T entity) {
        return entity.getId();
    }

    @Override
    protected InstancioApi<T> initInstancioApi() {
        InstancioApi<T> result = super.initInstancioApi();
        result.ignore(field(T::getId));
        return result;
    }

    @Override
    protected String getIdFieldName() {
        return "id";
    }
}
