package com.nickesqueda.grocerystoredemo.security;

import com.nickesqueda.grocerystoredemo.dto.UserDto;
import lombok.Getter;

public final class SessionContext {

  @Getter private static UserDto sessionUser;
  @Getter private static boolean isSessionActive;

  public static void setSessionContext(UserDto userDto) {
    if (isSessionActive) {
      throw new IllegalStateException(
          "Session is already active - cannot set session context again");
    }

    isSessionActive = true;
    sessionUser = userDto;
  }

  public static void updateSessionContext(UserDto userDto) {
    if (!isSessionActive) {
      throw new IllegalStateException("Session is not active - cannot update session context");
    }
    if (!userDto.getId().equals(sessionUser.getId())) {
      throw new IllegalArgumentException("Cannot update session - input user ID does not match session user ID");
    }

    sessionUser = userDto;
  }

  public static void clearSession() {
    isSessionActive = false;
    sessionUser = null;
  }
}
