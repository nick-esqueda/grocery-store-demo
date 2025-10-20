package com.nickesqueda.grocerystoredemo.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.nickesqueda.grocerystoredemo.dto.StoreDto;
import com.nickesqueda.grocerystoredemo.dto.UserDto;
import com.nickesqueda.grocerystoredemo.model.dao.Dao;
import com.nickesqueda.grocerystoredemo.model.entity.Store;
import com.nickesqueda.grocerystoredemo.model.entity.User;
import com.nickesqueda.grocerystoredemo.security.SessionContext;
import com.nickesqueda.grocerystoredemo.service.StoreService;
import com.nickesqueda.grocerystoredemo.testutils.BaseDataAccessTest;
import com.nickesqueda.grocerystoredemo.testutils.DbTestUtils;
import com.nickesqueda.grocerystoredemo.testutils.EntityTestUtils;
import com.nickesqueda.grocerystoredemo.util.ModelMapperUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.function.ThrowingSupplier;

public class StoreServiceIntegrationTest extends BaseDataAccessTest {

  private static StoreService storeService;
  private StoreDto testStoreDto;
  private UserDto adminDto;
  private UserDto customerDto;

  @BeforeAll
  static void setUp() {
    Dao<Store> storeDao = new Dao<>(Store.class);
    storeService = new StoreService(storeDao);
  }

  @BeforeEach
  void beforeEach() {
    this.adminDto = createAdminUser();
    this.customerDto = createCustomerUser();
    this.testStoreDto = createTestStore();
  }

  UserDto createAdminUser() {
    User adminUser = EntityTestUtils.createRandomAdminUser();
    DbTestUtils.persistEntity(adminUser);
    return ModelMapperUtil.map(adminUser, UserDto.class);
  }

  UserDto createCustomerUser() {
    User customer = EntityTestUtils.createRandomUser();
    DbTestUtils.persistEntity(customer);
    return ModelMapperUtil.map(customer, UserDto.class);
  }

  StoreDto createTestStore() {
    Store store = EntityTestUtils.createRandomStore();
    DbTestUtils.persistEntity(store);
    return ModelMapperUtil.map(store, StoreDto.class);
  }

  @AfterEach
  void clearSession() {
    SessionContext.clearSession();
  }

  @Test
  void getStoreDetailsTest() {
    ThrowingSupplier<StoreDto> action = () -> storeService.getStoreDetails(testStoreDto.getId());
    StoreDto result = assertDoesNotThrow(action);
    assertEquals(testStoreDto, result);
  }

  @Test
  void createStoreTest() {
    // Authenticate with admin user
    SessionContext.setSessionContext(adminDto);

    // Create input
    StoreDto newStoreDto = StoreDto.builder().address("test").totalPickupSpots(10).build();

    // Run the test
    ThrowingSupplier<StoreDto> action = () -> storeService.createStore(newStoreDto);
    StoreDto result = assertDoesNotThrow(action);

    // Verify returned DTO has the new ID and proper field values
    assertNotNull(result);
    assertNotNull(result.getId());
    assertEquals(newStoreDto.getAddress(), result.getAddress());
    assertEquals(newStoreDto.getTotalPickupSpots(), result.getTotalPickupSpots());

    // Verify the DB contains the newly created store
    Store newStoreFromDb = DbTestUtils.findEntityByValue(Store.class, "id", result.getId());
    assertNotNull(newStoreFromDb);
    assertEquals(newStoreFromDb.getAddress(), result.getAddress());
    assertEquals(newStoreFromDb.getTotalPickupSpots(), result.getTotalPickupSpots());
  }

  @Test
  void updateStoreTest() {
    // Authenticate with admin user
    SessionContext.setSessionContext(adminDto);

    // Create input
    testStoreDto.setAddress("test2");
    testStoreDto.setTotalPickupSpots(20);

    // Run the test
    Executable action = () -> storeService.updateStoreDetails(testStoreDto);
    assertDoesNotThrow(action);

    // Validate store values were updated in the database
    Store storeFromDb = DbTestUtils.findEntityByValue(Store.class, "id", testStoreDto.getId());
    assertNotNull(storeFromDb);
    assertEquals(testStoreDto.getAddress(), storeFromDb.getAddress());
    assertEquals(testStoreDto.getTotalPickupSpots(), storeFromDb.getTotalPickupSpots());
  }

  @Test
  void deleteStoreTest() {
    // Authenticate with admin user
    SessionContext.setSessionContext(adminDto);

    // Run the test
    Executable action = () -> storeService.deleteStore(testStoreDto.getId());
    assertDoesNotThrow(action);

    // Validate store was deleted from the database
    Store storeFromDb = DbTestUtils.findEntityByValue(Store.class, "id", testStoreDto.getId());
    assertNull(storeFromDb);
  }
}
