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

  public static void clearSession() {
    isSessionActive = false;
    sessionUser = null;
  }
}
