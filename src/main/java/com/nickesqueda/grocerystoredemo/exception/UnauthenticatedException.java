package com.nickesqueda.grocerystoredemo.exception;

public class UnauthenticatedException extends RuntimeException {

  public UnauthenticatedException() {
    super("Post-login feature requires authentication.");
  }
}
