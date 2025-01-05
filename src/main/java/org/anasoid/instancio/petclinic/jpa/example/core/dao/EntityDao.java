package org.anasoid.instancio.petclinic.jpa.example.core.dao;

import jakarta.persistence.*;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

@Repository
public class EntityDao<T, ID> {

    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    @Getter
    private EntityManager entityManager;

    @Transactional(Transactional.TxType.REQUIRED)
    public <F extends T> void persist(F entity) {
        entityManager.persist(entity);
    }


    public Query createQuery(String query) {
        return entityManager.createQuery(query);
    }

    public <F extends T> F find(Class<F> clazz, ID id) {
        return entityManager.find(clazz, id);
    }


    public T getEntityByQuery(Class<T> clazz, String query, Map<String, Object> params) {
        try {
            String finalQuery = MessageFormat.format(query, clazz.getSimpleName());
            Query queryJpa = this.createQuery(finalQuery);
            params.forEach(
                    (k, v) -> queryJpa.setParameter(k, v));
            return (T) queryJpa.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    public <F extends T> long getCountFromDatabase(Class<F> clazz) {
        String finalQuery =
                MessageFormat.format("select count(*) from {0}", clazz.getSimpleName());
        List<Long> resultsDatabase = this.createQuery(finalQuery).getResultList();

        return resultsDatabase.get(0);
    }
}
