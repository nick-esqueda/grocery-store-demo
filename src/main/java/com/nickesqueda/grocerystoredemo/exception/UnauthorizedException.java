package com.nickesqueda.grocerystoredemo.exception;

import com.nickesqueda.grocerystoredemo.model.entity.RoleName;

public class UnauthorizedException extends RuntimeException {

  public UnauthorizedException(Integer subjectId, Integer sessionUserId) {
    super(
        "Unauthorized action - Subject ID %d does not match Session User ID %d."
            .formatted(subjectId, sessionUserId));
  }

  public UnauthorizedException(Integer subjectId, RoleName expectedRole) {
    super(
        "Unauthorized action - Subject ID %d does not have role %s."
            .formatted(subjectId, expectedRole));
  }
}
