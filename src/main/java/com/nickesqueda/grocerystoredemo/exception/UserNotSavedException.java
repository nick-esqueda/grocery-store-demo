package com.nickesqueda.grocerystoredemo.exception;

public class UserNotSavedException extends RuntimeException {
  public UserNotSavedException(Throwable throwable) {
    super(throwable);
  }
}
