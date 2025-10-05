package com.nickesqueda.model.user;

import static com.nickesqueda.testutils.DbTestUtils.findEntityByValue;
import static com.nickesqueda.testutils.DbTestUtils.persistEntity;
import static com.nickesqueda.testutils.TestConstants.TEST_ADDRESS;
import static com.nickesqueda.testutils.TestConstants.TEST_USERNAME;
import static org.junit.jupiter.api.Assertions.*;

import com.nickesqueda.model.GenericDao;
import com.nickesqueda.testutils.BaseDaoTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

class UserGenericDaoTest extends BaseDaoTest {

  private final GenericDao<User> userGenericDao = new GenericDao<>(User.class);

  // Usage of this method is necessary to avoid "detached entity" states.
  private User createTestUser() {
    return User.builder()
        .username(TEST_USERNAME)
        .firstName("Test")
        .lastName("Test")
        .address(TEST_ADDRESS)
        .email("test@test.com")
        .phoneNumber("123-456-7890")
        .build();
  }

  @Test
  void testSave() {
    assertDoesNotThrow(() -> userGenericDao.save(createTestUser()));

    User result = findEntityByValue(User.class, "username", TEST_USERNAME);
    assertNotNull(result);
    assertEquals(TEST_USERNAME, result.getUsername());
  }

  @Test
  void testFindOneByValue() {
    persistEntity(createTestUser());

    ThrowingSupplier<User> action = () -> userGenericDao.findOneByValue("username", TEST_USERNAME);

    User result = assertDoesNotThrow(action);
    assertNotNull(result);
    assertEquals(TEST_USERNAME, result.getUsername());
  }

  @Test
  void testUpdate() {
    User user = createTestUser();
    persistEntity(user);
    String newUsername = "test2";

    user.setUsername(newUsername);
    assertDoesNotThrow(() -> userGenericDao.update(user));

    User result = findEntityByValue(User.class, "username", newUsername);
    assertNotNull(result);
    assertEquals(newUsername, result.getUsername());
  }

  @Test
  void testDelete() {
    User user = createTestUser();
    persistEntity(user);

    assertDoesNotThrow(() -> userGenericDao.delete(user));

    User result = findEntityByValue(User.class, "username", TEST_USERNAME);
    assertNull(result);
  }
}
