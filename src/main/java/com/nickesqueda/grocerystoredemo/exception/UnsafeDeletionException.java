package com.nickesqueda.grocerystoredemo.exception;

public class UnsafeDeletionException extends RuntimeException {

  public UnsafeDeletionException(String message) {
    super(message);
  }
}
