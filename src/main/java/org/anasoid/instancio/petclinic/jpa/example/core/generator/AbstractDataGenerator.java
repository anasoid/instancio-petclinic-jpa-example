package org.anasoid.instancio.petclinic.jpa.example.core.generator;


import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
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
        return this.getClass().getSimpleName().replace("EntityDataGenerator", " EntityDataGenerator");
    }

    @Override
    public void generate() {
        long element =
                Math.max(
                        minElement(),
                        Math.min(maxElement(), properties.getDataSize() * percentElement() / 100));
        log.info(">>>> Start generate {} of {}", element, getEntityClass().getSimpleName());
        long finalElement = element;
        if (!forceGenerateElement()) {
            long existCount = getCountFromDatabase();
            finalElement = existCount > element ? 0 : Math.min(element, element - existCount);
        }
        for (int i = 0; i < finalElement; i++) {
            generateElement();
        }
        log.info(">>>> End generate {} of {}", finalElement, getEntityClass().getSimpleName());
    }

    public void generateElement() {
        T entity = Instancio.of(getEntityModel(initInstancioApi())).create();

        T old = getEntityById(getId(entity));
        if (old == null) {
            entityDao.persist(entity);
        } else {
            log.info(
                    ">>>>>> skip persist {} with id {}",
                    getEntityClass().getSimpleName(),
                    getId(entity));
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

    protected T getEntityById(ID id) {
        String finalQuery =
                MessageFormat.format(
                        "select e from {0} e where id= ?1", getEntityClass().getSimpleName());
        try {
            return (T) entityDao.createQuery(finalQuery).setParameter(1, id).getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }


    protected long getCountFromDatabase() {
        String finalQuery =
                MessageFormat.format("select count(*) from {0}", getEntityClass().getSimpleName());
        List<Long> resultsDatabase = entityDao.createQuery(finalQuery).getResultList();

        return resultsDatabase.get(0);
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
        return "select id from {0}";
    }

    protected abstract Model<T> getEntityModel(InstancioApi<T> instancioApi);

    protected abstract Class<T> getEntityClass();

    protected abstract int minElement();

    protected abstract int maxElement();

    protected abstract int percentElement();

    protected abstract boolean forceGenerateElement();

    protected abstract ID getId(T entity);
}
