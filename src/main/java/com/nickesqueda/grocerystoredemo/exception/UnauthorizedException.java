package com.nickesqueda.grocerystoredemo.exception;

public class UnauthorizedException extends RuntimeException {

  public UnauthorizedException(Integer subjectId, Integer sessionUserId) {
    super("Unauthorized action - Subject ID %d does not match Session User ID %d.".formatted(subjectId, sessionUserId));
  }
}
