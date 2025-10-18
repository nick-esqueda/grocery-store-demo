package com.nickesqueda.grocerystoredemo.exception;

public class EntityNotUpdatedException extends RuntimeException {

  public <T> EntityNotUpdatedException(Class<T> entityType, Throwable cause) {
    super(entityType.getSimpleName() + " could not be updated in the database.", cause);
  }
}
