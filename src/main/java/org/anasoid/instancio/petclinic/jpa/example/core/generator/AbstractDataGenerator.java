package org.anasoid.instancio.petclinic.jpa.example.core.generator;


import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.anasoid.instancio.petclinic.jpa.example.core.GeneratorConfig;
import org.anasoid.instancio.petclinic.jpa.example.core.dao.EntityDao;
import org.anasoid.instancio.petclinic.jpa.example.core.properties.SampleDataProperties;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Model;
import org.instancio.settings.FeedDataAccess;
import org.instancio.settings.Keys;
import org.instancio.settings.Settings;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.nio.file.Path;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.IntStream;

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
        if (!getGeneratorConfig().isForceGenerateElement()) {
            long existCount = getCountFromDatabase();
            finalElement = existCount > element ? 0 : Math.min(element, element - existCount);
        }
        for (int i = 0; i < finalElement; i++) {
            generateElement();
        }
        log.info(">>>> End generate {} of {}", finalElement, getEntityClass().getSimpleName());
    }

    @Override
    public List<T> generate(int min, int max) {
        Random random = new Random();
        int element = random.nextInt(max - min + 1) + min;
        log.info(">>>> Start generate  [{},{}] of {}", min, max, getEntityClass().getSimpleName());
        log.info(">>>> Start generate {} of {}", element, getEntityClass().getSimpleName());
        List<T> result = new ArrayList<>(element);
        for (int i = 0; i < element; i++) {
            result.add(generateElement());
        }
        log.info(">>>> End generate {} of {}", element, getEntityClass().getSimpleName());
        return result;
    }

    public T generateElement() {
        T entity = Instancio.of(getEntityModel(initInstancioApi())).create();

        T old = getEntityByFunctionalId(entity);
        if (old == null) {
            entityDao.persist(entity);
            return entity;
        } else {
            log.info(
                    ">>>>>> skip persist {} with {} {}",
                    getEntityClass().getSimpleName(),
                    getIdFieldName(),
                    getId(entity));
            return old;
        }

    }

    protected InstancioApi<T> initInstancioApi() {
        InstancioApi<T> instancio =
                Instancio.of(getEntityClass())
                        .withSettings(getSettings());
        applyFeed(instancio);
        return instancio;
    }

    private void applyFeed(InstancioApi<T> instancio) {
        String resourcePath = "/data/" + getEntityClass().getSimpleName() + ".csv";
        URL csv = this.getClass().getResource(resourcePath);
        if (csv != null) {
            instancio.applyFeed(
                    all(this.getEntityClass()),
                    feed -> feed.ofFile(Path.of(csv.getFile())).dataAccess(FeedDataAccess.RANDOM));
        } else {
            log.info("Skip csv feed ressource '{}' not found", resourcePath);
        }
    }

    protected Settings getSettings() {
        return Settings.create().set(Keys.MAX_DEPTH, 2);
    }

    protected T getRandomFromDatabase() {
        List<T> result = getRandomFromDatabase(1, 1);
        return result.isEmpty() ? null : result.get(0);
    }

    protected List<T> getRandomFromDatabase(int max) {

        return getRandomFromDatabase(1, max);
    }

    protected List<T> getRandomFromDatabase(int min, int max) {

        return getRandomFromDatabase(getEntityClass(), min, max);
    }

    protected <F extends T> List<F> getRandomFromDatabase(Class<F> clazz, int min, int max) {

        return getRandomFromDatabase(clazz, getQueryDefault(), min, max);
    }

    protected T getEntityByFunctionalId(T entity) {

        return entityDao.getEntityByQuery(getEntityClass(), getFunctionalIdQuery(), getFunctionalIdParams(entity));

    }
    protected abstract Map<String, Object> getFunctionalIdParams(T entity);
    protected abstract String   getFunctionalIdQuery();


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

    protected abstract GeneratorConfig getGeneratorConfig();


    protected abstract ID getId(T entity);

    protected abstract String getIdFieldName();
}
