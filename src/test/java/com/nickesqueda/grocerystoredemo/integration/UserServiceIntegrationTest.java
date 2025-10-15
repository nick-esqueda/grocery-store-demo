package com.nickesqueda.grocerystoredemo.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nickesqueda.grocerystoredemo.dto.UserDto;
import com.nickesqueda.grocerystoredemo.exception.UnauthenticatedException;
import com.nickesqueda.grocerystoredemo.exception.UnauthorizedException;
import com.nickesqueda.grocerystoredemo.model.dao.Dao;
import com.nickesqueda.grocerystoredemo.model.dao.ReadOnlyDao;
import com.nickesqueda.grocerystoredemo.model.entity.Role;
import com.nickesqueda.grocerystoredemo.model.entity.User;
import com.nickesqueda.grocerystoredemo.security.SessionContext;
import com.nickesqueda.grocerystoredemo.service.AuthService;
import com.nickesqueda.grocerystoredemo.service.UserService;
import com.nickesqueda.grocerystoredemo.testutils.BaseDataAccessTest;
import com.nickesqueda.grocerystoredemo.testutils.DbTestUtils;
import com.nickesqueda.grocerystoredemo.testutils.EntityTestUtils;
import com.nickesqueda.grocerystoredemo.util.ModelMapperUtil;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class UserServiceIntegrationTest extends BaseDataAccessTest {

  private static UserService userService;
  private UserDto userDto;

  @BeforeAll
  static void setUp() {
    Dao<User> userDao = new Dao<>(User.class);
    ReadOnlyDao<Role> roleDao = new ReadOnlyDao<>(Role.class);
    var authService = new AuthService(roleDao, userDao);
    userService = new UserService(userDao, authService);
  }

  @BeforeEach
  void createTestUser() {
    String rawPassword = UUID.randomUUID().toString();
    User user = EntityTestUtils.createRandomUser(rawPassword);
    DbTestUtils.persistEntity(user);
    userDto = ModelMapperUtil.map(user, UserDto.class);
  }

  @BeforeEach
  void authenticateUser() {
    SessionContext.setSessionContext(userDto);
  }

  @AfterEach
  void cleanup() {
    SessionContext.clearSession();
  }

  @Test
  void updateUserDataTest() {
    // Update username on DTO for test input
    String newUsername = "updated";
    userDto.setUsername(newUsername);

    // Run the test
    Executable action = () -> userService.updateUserData(userDto);
    assertDoesNotThrow(action);

    // Fetch user from database for validation
    User userEntity = DbTestUtils.findEntityByValue(User.class, "id", userDto.getId());
    assertNotNull(userEntity);
    UserDto userFromDbDto = ModelMapperUtil.map(userEntity, UserDto.class);

    // Verify the database entity contains the updated username and other important fields match
    assertEquals(newUsername, userEntity.getUsername());
    assertEquals(userDto.getId(), userFromDbDto.getId());
    assertEquals(userDto.getRoles(), userFromDbDto.getRoles());

    // Ensure SessionContext was updated
    assertNotNull(SessionContext.getSessionUser());
    assertEquals(newUsername, SessionContext.getSessionUser().getUsername());
  }

  @Test
  void updateUserData_ShouldThrow_WhenSessionInactive() {
    // Clear session
    SessionContext.clearSession();

    // Ensure session is NOT active before test
    assertFalse(SessionContext.isSessionActive());
    assertNull(SessionContext.getSessionUser());

    // Update username on DTO for test input
    String newUsername = "updated";
    userDto.setUsername(newUsername);

    // Run the test
    Executable action = () -> userService.updateUserData(userDto);
    assertThrows(UnauthenticatedException.class, action);
  }

  @Test
  void updateUserData_ShouldThrow_GivenMismatchedUser() {
    // Create another user to act as a fraud (original user will still be in SessionContext)
    User badUser = EntityTestUtils.createRandomUser();
    DbTestUtils.persistEntity(badUser);
    UserDto badUserDto = ModelMapperUtil.map(badUser, UserDto.class);

    // Run the test
    Executable action = () -> userService.updateUserData(badUserDto);
    assertThrows(UnauthorizedException.class, action);
  }

  @Test
  void deleteAccountTest() {
    // Run the test
    Executable action = () -> userService.deleteAccount();
    assertDoesNotThrow(action);

    // Verify the user was removed from the database
    User userEntity = DbTestUtils.findEntityByValue(User.class, "id", userDto.getId());
    assertNull(userEntity);
  }
}
