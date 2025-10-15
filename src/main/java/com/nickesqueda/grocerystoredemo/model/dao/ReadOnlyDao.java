package com.nickesqueda.grocerystoredemo.model.dao;

import com.nickesqueda.grocerystoredemo.exception.EntityNotFoundException;
import com.nickesqueda.grocerystoredemo.model.entity.BaseEntity;
import com.nickesqueda.grocerystoredemo.model.util.HibernateUtil;
import jakarta.persistence.NoResultException;
import java.util.List;
import org.hibernate.Session;

public class ReadOnlyDao<T extends BaseEntity> {

  private final Class<T> entityClass;

  public ReadOnlyDao(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  public <U> T findOneByValue(String fieldName, U value) {
    String query = "FROM " + entityClass.getSimpleName() + " e WHERE e." + fieldName + " = :value";

    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      return session.createQuery(query, entityClass).setParameter("value", value).getSingleResult();
    } catch (NoResultException ex) {
      throw new EntityNotFoundException(entityClass, value.toString(), ex);
    }
  }

  public <U> T findOneByValueWithChildren(String fieldName, U value, String childFieldName) {
    String query =
        "FROM %s e LEFT JOIN FETCH e.%s r WHERE e.%s = :value"
            .formatted(entityClass.getSimpleName(), childFieldName, fieldName);

    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      return session.createQuery(query, entityClass).setParameter("value", value).getSingleResult();
    } catch (NoResultException ex) {
      throw new EntityNotFoundException(entityClass, value.toString(), ex);
    }
  }

  public List<T> findAll() {
    String query = "FROM " + entityClass.getSimpleName();
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      return session.createQuery(query, entityClass).getResultList();
    }
  }
}
