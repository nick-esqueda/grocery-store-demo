package com.nickesqueda.grocerystoredemo.exception;

public class NoAuthRequiredException extends RuntimeException {

  public NoAuthRequiredException() {
    super("Pre-login feature requires the absence of a session.");
  }
}
