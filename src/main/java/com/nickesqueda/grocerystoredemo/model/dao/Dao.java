package com.nickesqueda.grocerystoredemo.model.dao;

import com.nickesqueda.grocerystoredemo.model.entity.BaseEntity;
import com.nickesqueda.grocerystoredemo.model.util.HibernateUtil;

public class Dao<T extends BaseEntity> extends ReadOnlyDao<T> {

  public Dao(Class<T> entityClass) {
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
