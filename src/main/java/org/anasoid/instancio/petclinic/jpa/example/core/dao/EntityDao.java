package org.anasoid.instancio.petclinic.jpa.example.core.dao;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.PersistenceContextType;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import lombok.Getter;
import org.springframework.stereotype.Repository;

@Repository
public class EntityDao<T, I> {

    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    @Getter
    private EntityManager entityManager;

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public <F extends T> void persist(F entity) {
        entityManager.persist(entity);
    }

    public Query createQuery(String query) {
        return entityManager.createQuery(query);
    }

    public <F extends T> F find(Class<F> clazz, I id) {
        return entityManager.find(clazz, id);
    }


}
