package com.nickesqueda.grocerystoredemo.exception;

public class InsufficientInventoryException extends RuntimeException {

  public InsufficientInventoryException(String message) {
    super(message);
  }
}
