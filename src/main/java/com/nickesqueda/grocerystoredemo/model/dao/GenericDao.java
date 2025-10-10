package com.nickesqueda.grocerystoredemo.model.dao;

import com.nickesqueda.grocerystoredemo.model.entity.BaseEntity;
import com.nickesqueda.grocerystoredemo.model.util.HibernateUtil;

public class GenericDao<T extends BaseEntity> extends GenericReadOnlyDao<T> {

  public GenericDao(Class<T> entityClass) {
    super(entityClass);
  }

  public void save(T entity) {
    HibernateUtil.executeInTransaction(session -> session.persist(entity));
  }

  public void update(T entity) {
    HibernateUtil.executeInTransaction(session -> session.merge(entity));
  }

  public void delete(T entity) {
    HibernateUtil.executeInTransaction(session -> session.remove(entity));
  }
}
