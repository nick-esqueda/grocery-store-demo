package com.nickesqueda.grocerystoredemo.model.dao;

import com.nickesqueda.grocerystoredemo.exception.EntityNotFoundException;
import com.nickesqueda.grocerystoredemo.model.entity.BaseEntity;
import com.nickesqueda.grocerystoredemo.model.util.HibernateUtil;
import jakarta.persistence.NoResultException;
import org.hibernate.Session;

public class GenericReadOnlyDao<T extends BaseEntity> {

  private final Class<T> entityClass;

  public GenericReadOnlyDao(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  public <U> T findOneByValue(String columnName, U value) {
    String query = "FROM " + entityClass.getSimpleName() + " e WHERE e." + columnName + " = :value";
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      return session.createQuery(query, entityClass).setParameter("value", value).getSingleResult();
    } catch (NoResultException ex) {
      throw new EntityNotFoundException(entityClass, value.toString(), ex);
    }
  }
}