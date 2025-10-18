package com.nickesqueda.grocerystoredemo.exception;

public class EntityNotFoundException extends RuntimeException {

  public <T> EntityNotFoundException(Class<T> entityType, Throwable cause) {
    super(entityType.getSimpleName() + " was not found in the database.", cause);
  }
}
