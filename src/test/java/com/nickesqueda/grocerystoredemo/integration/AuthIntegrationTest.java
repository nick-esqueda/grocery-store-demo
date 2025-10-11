package com.nickesqueda.grocerystoredemo.integration;

import static org.junit.jupiter.api.Assertions.*;

import com.nickesqueda.grocerystoredemo.dto.UserCredentialsDto;
import com.nickesqueda.grocerystoredemo.dto.UserDto;
import com.nickesqueda.grocerystoredemo.model.dao.Dao;
import com.nickesqueda.grocerystoredemo.model.dao.ReadOnlyDao;
import com.nickesqueda.grocerystoredemo.model.entity.Role;
import com.nickesqueda.grocerystoredemo.model.entity.User;
import com.nickesqueda.grocerystoredemo.security.SessionContext;
import com.nickesqueda.grocerystoredemo.service.AuthService;
import com.nickesqueda.grocerystoredemo.testutils.BaseDataAccessTest;
import com.nickesqueda.grocerystoredemo.testutils.DbTestUtils;
import com.nickesqueda.grocerystoredemo.testutils.EntityTestUtils;
import com.nickesqueda.grocerystoredemo.util.ModelMapperUtil;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class AuthIntegrationTest extends BaseDataAccessTest {

  private static AuthService authService;

  @BeforeAll
  static void setup() {
    Dao<User> userDao = new Dao<>(User.class);
    ReadOnlyDao<Role> roleDao = new ReadOnlyDao<>(Role.class);
    authService = new AuthService(roleDao, userDao);
  }

  @AfterEach
  void cleanUp() {
    SessionContext.clearSession();
  }

  @Test
  void registrationTest() {
    assertFalse(SessionContext.isSessionActive());
    assertNull(SessionContext.getSessionUser());

    UserDto userDto = EntityTestUtils.createRandomUserDto();
    String rawPassword = UUID.randomUUID().toString();

    Executable action = () -> authService.registerUser(userDto, rawPassword);
    assertDoesNotThrow(action);

    assertTrue(SessionContext.isSessionActive());
    assertNotNull(SessionContext.getSessionUser());
    assertEquals(userDto, SessionContext.getSessionUser());
  }

  @Test
  void authenticationTest() {
    String rawPassword = UUID.randomUUID().toString();
    User testUser = EntityTestUtils.createRandomUser(rawPassword);
    DbTestUtils.persistEntity(testUser);

    assertFalse(SessionContext.isSessionActive());
    assertNull(SessionContext.getSessionUser());

    var credentials = new UserCredentialsDto(testUser.getUsername(), rawPassword);
    Executable action = () -> authService.authenticateUser(credentials);
    assertDoesNotThrow(action);

    assertTrue(SessionContext.isSessionActive());
    assertNotNull(SessionContext.getSessionUser());
  }

  @Test
  void logoutTest() {
    String rawPassword = UUID.randomUUID().toString();
    User user = EntityTestUtils.createRandomUser(rawPassword);
    DbTestUtils.persistEntity(user);
    UserDto userDto = ModelMapperUtil.map(user, UserDto.class);

    SessionContext.setSessionContext(userDto);

    assertTrue(SessionContext.isSessionActive());
    assertNotNull(SessionContext.getSessionUser());

    Executable action = () -> authService.logOut();
    assertDoesNotThrow(action);

    assertFalse(SessionContext.isSessionActive());
    assertNull(SessionContext.getSessionUser());
  }
}
