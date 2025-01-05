package org.anasoid.instancio.petclinic.jpa.example.core.helper;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.anasoid.instancio.petclinic.jpa.example.core.dao.EntityDao;
import org.anasoid.instancio.petclinic.jpa.example.core.properties.SampleDataProperties;
import org.instancio.feed.FeedProvider;
import org.instancio.settings.FeedDataAccess;

import java.net.URL;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

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
@RequiredArgsConstructor
@Slf4j
public class DataGeneratorHelper<T, ID> {

    @Getter
    private final EntityDao<T, ID> entityDao;
    private final SampleDataProperties properties;
    @Getter
    protected final Class<T> entityClass;

    public <F extends T> List<F> getRandomFromDatabase(
            Class<F> clazz, String query, int min, int max) {
        String finalQuery = MessageFormat.format(query, clazz.getSimpleName());
        List<ID> resultsDatabase = entityDao.createQuery(finalQuery).getResultList();
        Random random = new Random();
        int randomSize = random.nextInt(max - min + 1) + min;
        randomSize = Math.min(randomSize, resultsDatabase.size());
        List<ID> results = new ArrayList<>();
        IntStream.range(0, randomSize)
                .forEach(
                        n -> {
                            Random random2 = new Random();
                            int randomIndex = random2.nextInt(max - min + 1) + min;
                            randomIndex = Math.min(randomIndex, resultsDatabase.size() - 1);
                            results.add(resultsDatabase.get(randomIndex));
                        });

        return results.stream().map(id -> entityDao.find(clazz, id)).toList();
    }

    public long getCountFromDatabase() {

        return entityDao.getCountFromDatabase(getEntityClass());
    }

    public FeedProvider getFeedProvider() {
        String resourcePath = properties.getCsvPath() + "/" + getEntityClass().getSimpleName().toLowerCase() + ".csv";
        URL csv = this.getClass().getResource(resourcePath);
        if (csv != null) {
            return feed -> feed.ofFile(Path.of(csv.getFile())).dataAccess(FeedDataAccess.RANDOM);

        } else {
            log.info("Skip csv feed ressource '{}' not found", resourcePath);
            return null;
        }
    }


}
