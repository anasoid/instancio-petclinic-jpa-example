package org.anasoid.instancio.petclinic.jpa.example.core.generator;


import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.anasoid.instancio.petclinic.jpa.example.core.dao.EntityDao;
import org.anasoid.instancio.petclinic.jpa.example.core.properties.SampleDataProperties;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Model;
import org.instancio.feed.FeedProvider;
import org.instancio.settings.FeedDataAccess;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.instancio.Select.all;

@Slf4j
@Transactional
public abstract class AbstractDataGenerator<T, ID> implements DataGenerator {

    @Autowired
    @Getter
    private EntityDao<T, ID> entityDao;
    @Autowired
    @Getter
    private SampleDataProperties properties;

    @Override
    public String getName() {
        return this.getClass().getSimpleName().replace("EntityDataGenerator", " DataGenerator");
    }

    @Override
    public void generate() {
        long element =
                Math.max(
                        getGeneratorConfig().getMinElement(),
                        Math.min(getGeneratorConfig().getMaxElement(), properties.getDataSize() * getGeneratorConfig().getMinElement() / 100));
        log.info(">>>> Start generate {} of {}", element, getEntityClass().getSimpleName());
        long finalElement = element;
        if (!getGeneratorConfig().getForceGenerateElement()) {
            long existCount = getCountFromDatabase();
            finalElement = existCount > element ? 0 : Math.min(element, element - existCount);
        }
        //force generate Elemnt by count
        long count = generateElementStream(finalElement).count();
        log.info(">>>> End generate {} of {}", count, getEntityClass().getSimpleName());
    }

    @Override
    public List<T> generate(int min, int max) {
        Random random = new Random();
        int element = random.nextInt(max - min + 1) + min;
        log.info(">>>> Start generate  [{},{}] of {}", min, max, getEntityClass().getSimpleName());
        log.info(">>>> Start generate {} of {}", element, getEntityClass().getSimpleName());
        List<T> result = generateElementStream(element).toList();
        log.info(">>>> End generate {} of {}", element, getEntityClass().getSimpleName());
        return result;
    }

    private Stream<T> generateElementStream(long maxSize) {
        return Instancio.stream(getEntityModel(initInstancioApi()))

                .limit(maxSize)
                .map(entity -> {
                    T old = getEntityByFunctionalId(entity);
                    if (old == null) {
                        entityDao.persist(entity);
                        return entity;
                    } else {
                        log.info(
                                ">>>>>> skip persist {} with {}",
                                getEntityClass().getSimpleName(),
                                getFunctionalIdParams(old));
                        return old;
                    }
                });
    }


    protected InstancioApi<T> initInstancioApi() {
        InstancioApi<T> instancio =
                Instancio.of(getEntityClass())
                        .withSettings(getSettings());
        applyFeed(instancio);
        return instancio;
    }

    private void applyFeed(InstancioApi<T> instancio) {
        FeedProvider feedProvider = getFeedProvider();
        if (feedProvider != null) {
            instancio.applyFeed(all(this.getEntityClass()), feedProvider);

        }
    }

    protected FeedProvider getFeedProvider() {
        String resourcePath = "/data/" + getEntityClass().getSimpleName() + ".csv";
        URL csv = this.getClass().getResource(resourcePath);
        if (csv != null) {
            return feed -> feed.ofFile(Path.of(csv.getFile())).dataAccess(FeedDataAccess.RANDOM);

        } else {
            log.info("Skip csv feed ressource '{}' not found", resourcePath);
            return null;
        }
    }

    protected Settings getSettings() {
        return Settings.create().set(Keys.MAX_DEPTH, 2);
    }

    public T getRandomFromDatabase() {
        List<T> result = getRandomFromDatabase(1, 1);
        return result.isEmpty() ? null : result.get(0);
    }

    public List<T> getRandomFromDatabase(int max) {

        return getRandomFromDatabase(1, max);
    }

    public List<T> getRandomFromDatabase(int min, int max) {

        return getRandomFromDatabase(getEntityClass(), min, max);
    }

    protected <F extends T> List<F> getRandomFromDatabase(Class<F> clazz, int min, int max) {

        return getRandomFromDatabase(clazz, getQueryDefault(), min, max);
    }

    protected T getEntityByFunctionalId(T entity) {

        return entityDao.getEntityByQuery(getEntityClass(), getFunctionalIdQuery(), getFunctionalIdParams(entity));

    }

    protected abstract Map<String, Object> getFunctionalIdParams(T entity);

    protected abstract String getFunctionalIdQuery();


    protected long getCountFromDatabase() {

        return entityDao.getCountFromDatabase(getEntityClass());
    }

    protected <F extends T> List<F> getRandomFromDatabase(
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
                            results.add(resultsDatabase.get(randomIndex));
                        });

        return results.stream().map(id -> entityDao.find(clazz, id)).toList();
    }

    protected String getQueryDefault() {
        return "select " + getIdFieldName() + " from {0} ";

    }

    protected abstract Model<T> getEntityModel(InstancioApi<T> instancioApi);

    protected abstract Class<T> getEntityClass();


    protected SampleDataProperties.GeneratorProperties getGeneratorConfig() {
        return getProperties().getEntities().get(getEntityClass().getSimpleName().toLowerCase());
    }


    protected abstract ID getId(T entity);

    protected abstract String getIdFieldName();
}
