package com.nickesqueda.grocerystoredemo.security;

import com.nickesqueda.grocerystoredemo.dto.UserDto;
import com.nickesqueda.grocerystoredemo.exception.NoAuthRequiredException;
import com.nickesqueda.grocerystoredemo.exception.UnauthenticatedException;
import com.nickesqueda.grocerystoredemo.exception.UnauthorizedException;
import com.nickesqueda.grocerystoredemo.model.entity.RoleName;

public final class AuthValidator {

  public static void requireAdminRole() {
    UserDto sessionUser = SessionContext.getSessionUser();
    if (!SessionContext.isSessionActive() || sessionUser == null) {
      throw new UnauthenticatedException();
    }
    if (!sessionUser.getRoles().contains(RoleName.ROLE_ADMIN)) {
      throw new UnauthorizedException(sessionUser.getId(), RoleName.ROLE_ADMIN);
    }
  }

  public static void requireNoAuth() {
    if (SessionContext.isSessionActive()) {
      throw new NoAuthRequiredException();
    }
  }
}
