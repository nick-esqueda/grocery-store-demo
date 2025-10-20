package com.nickesqueda.grocerystoredemo.exception;

public class SessionInactiveException extends RuntimeException {

  public SessionInactiveException(String message) {
    super(message);
  }
}
