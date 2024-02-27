package com.example.server.localization;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class LocalizationRepository {

    @PersistenceContext
    private EntityManager entityManager; // == session

    @Transactional
    public Localization save(Localization localization) {
        entityManager.persist(localization);
        return localization;
    }

    public List<Localization> findAll() {
        TypedQuery<Localization> query = entityManager.createQuery("select l from Localization l", Localization.class);
        List<Localization> localizations = query.getResultList();
        return localizations;
    }

    public Localization findOne(long localizationId) {
        return entityManager.find(Localization.class, localizationId);
    }
}
