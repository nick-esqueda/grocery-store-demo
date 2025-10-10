package com.nickesqueda.grocerystoredemo.security;

import at.favre.lib.crypto.bcrypt.BCrypt;

public final class PasswordHasher {

  private PasswordHasher() {}

  public static String hash(String password) {
    return BCrypt.withDefaults().hashToString(12, password.toCharArray());
  }

  public static boolean compare(String rawPassword, String hashedPassword) {
    BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), hashedPassword);
    return result.verified;
  }
}
