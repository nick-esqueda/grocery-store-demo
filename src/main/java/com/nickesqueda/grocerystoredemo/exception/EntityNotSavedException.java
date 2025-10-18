package com.nickesqueda.grocerystoredemo.exception;

public class EntityNotSavedException extends RuntimeException {

  public <T> EntityNotSavedException(Class<T> entityType, Throwable cause) {
    super(entityType.getSimpleName() + " could not be saved to the database.", cause);
  }
}
