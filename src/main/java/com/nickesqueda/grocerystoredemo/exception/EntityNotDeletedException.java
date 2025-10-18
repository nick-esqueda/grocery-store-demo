package com.nickesqueda.grocerystoredemo.exception;

public class EntityNotDeletedException extends RuntimeException {

  public <T> EntityNotDeletedException(Class<T> entityType, Throwable cause) {
    super(entityType.getSimpleName() + " could not be deleted from the database.", cause);
  }
}
