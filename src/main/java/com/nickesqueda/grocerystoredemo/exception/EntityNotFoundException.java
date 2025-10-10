package com.nickesqueda.grocerystoredemo.exception;

public class EntityNotFoundException extends RuntimeException {

  public <T> EntityNotFoundException(Class<T> entityType, String identifier, Throwable cause) {
    super("%s '%s' was not found in the database.".formatted(entityType.getSimpleName(), identifier), cause);
  }
}
