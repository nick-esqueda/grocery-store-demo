package com.nickesqueda.grocerystoredemo.model.dao;

import com.nickesqueda.grocerystoredemo.exception.EntityNotDeletedException;
import com.nickesqueda.grocerystoredemo.exception.EntityNotSavedException;
import com.nickesqueda.grocerystoredemo.exception.EntityNotUpdatedException;
import com.nickesqueda.grocerystoredemo.model.entity.BaseEntity;
import com.nickesqueda.grocerystoredemo.model.util.HibernateUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Dao<T extends BaseEntity> extends ReadOnlyDao<T> {

  public Dao(Class<T> entityClass) {
    super(entityClass);
  }

  public void save(T entity) {
    try {
      HibernateUtil.executeInTransaction(session -> session.persist(entity));
    } catch (RuntimeException ex) {
      log.error(
          "{} could not be saved to the database. Entity: {} | Exception: ",
          entityClass.getSimpleName(),
          entity,
          ex);
      throw new EntityNotSavedException(entityClass, ex);
    }
  }

  public void update(T entity) {
    try {
      HibernateUtil.executeInTransaction(session -> session.merge(entity));
    } catch (RuntimeException ex) {
      log.error(
          "{} could not be updated in the database. Entity: {} | Exception: ",
          entityClass.getSimpleName(),
          entity,
          ex);
      throw new EntityNotUpdatedException(entityClass, ex);
    }
  }

  public void delete(T entity) {
    try {
      HibernateUtil.executeInTransaction(session -> session.remove(entity));
    } catch (RuntimeException ex) {
      log.error(
          "{} could not be deleted from the database. Entity: {} | Exception: ",
          entityClass.getSimpleName(),
          entity,
          ex);
      throw new EntityNotDeletedException(entityClass, ex);
    }
  }
}
