package com.nickesqueda.grocerystoredemo.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nickesqueda.grocerystoredemo.dto.InventoryItemDto;
import com.nickesqueda.grocerystoredemo.dto.UserDto;
import com.nickesqueda.grocerystoredemo.exception.InsufficientInventoryException;
import com.nickesqueda.grocerystoredemo.exception.UnsafeDeletionException;
import com.nickesqueda.grocerystoredemo.model.dao.Dao;
import com.nickesqueda.grocerystoredemo.model.entity.*;
import com.nickesqueda.grocerystoredemo.security.SessionContext;
import com.nickesqueda.grocerystoredemo.service.InventoryService;
import com.nickesqueda.grocerystoredemo.service.ProductService;
import com.nickesqueda.grocerystoredemo.service.StoreService;
import com.nickesqueda.grocerystoredemo.testutils.BaseDataAccessTest;
import com.nickesqueda.grocerystoredemo.testutils.DbTestUtils;
import com.nickesqueda.grocerystoredemo.testutils.EntityTestUtils;
import com.nickesqueda.grocerystoredemo.util.ModelMapperUtil;
import java.util.List;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.function.ThrowingSupplier;

public class InventoryServiceIntegrationTest extends BaseDataAccessTest {

  private static InventoryService inventoryService;
  private UserDto adminDto;
  private InventoryItemDto testInventoryItemDto;

  @BeforeAll
  static void setUp() {
    Dao<InventoryItem> inventoryItemDao = new Dao<>(InventoryItem.class);
    Dao<Store> storeDao = new Dao<>(Store.class);
    Dao<Product> productDao = new Dao<>(Product.class);
    Dao<Category> categoryDao = new Dao<>(Category.class);
    StoreService storeService = new StoreService(storeDao);
    ProductService productService = new ProductService(productDao, categoryDao);
    inventoryService = new InventoryService(inventoryItemDao, storeService, productService);
  }

  @BeforeEach
  void beforeEach() {
    this.adminDto = createAdminUser();
    this.testInventoryItemDto = createInventoryItemDto();
  }

  UserDto createAdminUser() {
    User adminUser = EntityTestUtils.createRandomAdminUser();
    DbTestUtils.persistEntity(adminUser);
    return ModelMapperUtil.map(adminUser, UserDto.class);
  }

  InventoryItemDto createInventoryItemDto() {
    InventoryItem inventoryItem = EntityTestUtils.createRandomInventoryItem(5, 0);
    DbTestUtils.persistEntity(inventoryItem);
    return ModelMapperUtil.map(inventoryItem, InventoryItemDto.class);
  }

  @AfterEach
  void clearSession() {
    SessionContext.clearSession();
  }

  @Test
  void getInventoryItemTest() {
    // No auth required

    ThrowingSupplier<InventoryItemDto> action =
        () -> inventoryService.getInventoryItem(testInventoryItemDto.getId());
    InventoryItemDto result = assertDoesNotThrow(action);
    assertEquals(testInventoryItemDto, result);
  }

  @Test
  void getStoreInventoryTest() {
    // No auth required

    // Create test store
    Store store = EntityTestUtils.createRandomStore();
    DbTestUtils.persistEntity(store);

    // Create test products
    Product product1 = EntityTestUtils.createRandomProduct();
    Product product2 = EntityTestUtils.createRandomProduct();
    Product product3 = EntityTestUtils.createRandomProduct();
    DbTestUtils.persistEntity(product1);
    DbTestUtils.persistEntity(product2);
    DbTestUtils.persistEntity(product3);

    // Create inventory items using above store & products
    InventoryItem inventoryItem1 =
        InventoryItem.builder()
            .store(store)
            .product(product1)
            .quantity(1)
            .quantityOnHold(0)
            .build();
    InventoryItem inventoryItem2 =
        InventoryItem.builder()
            .store(store)
            .product(product2)
            .quantity(5)
            .quantityOnHold(2)
            .build();
    InventoryItem inventoryItem3 =
        InventoryItem.builder()
            .store(store)
            .product(product3)
            .quantity(3)
            .quantityOnHold(3)
            .build();
    DbTestUtils.persistEntity(inventoryItem1);
    DbTestUtils.persistEntity(inventoryItem2);
    DbTestUtils.persistEntity(inventoryItem3);

    // Create DTOs out of the inventory items for comparison
    InventoryItemDto inventoryItemDto1 =
        ModelMapperUtil.map(inventoryItem1, InventoryItemDto.class);
    InventoryItemDto inventoryItemDto2 =
        ModelMapperUtil.map(inventoryItem1, InventoryItemDto.class);
    InventoryItemDto inventoryItemDto3 =
        ModelMapperUtil.map(inventoryItem1, InventoryItemDto.class);

    // Run the test
    ThrowingSupplier<List<InventoryItemDto>> action =
        () -> inventoryService.getStoreInventory(store.getId());
    List<InventoryItemDto> result = assertDoesNotThrow(action);

    // Verify each item in the result list matches one of the test inventory item DTOs
    assertNotNull(result);
    assertEquals(3, result.size());
    assertTrue(result.stream().anyMatch(item -> item.equals(inventoryItemDto1)));
    assertTrue(result.stream().anyMatch(item -> item.equals(inventoryItemDto2)));
    assertTrue(result.stream().anyMatch(item -> item.equals(inventoryItemDto3)));
  }

  @Test
  void addInventoryItemTest() {
    // Authenticate with admin user
    SessionContext.setSessionContext(adminDto);

    // Create store and product for inventory item associations
    Store store = EntityTestUtils.createRandomStore();
    Product product = EntityTestUtils.createRandomProduct();
    DbTestUtils.persistEntity(store);
    DbTestUtils.persistEntity(product);
    Integer quantity = 5;

    // Run the test
    ThrowingSupplier<InventoryItemDto> action =
        () -> inventoryService.addInventoryItem(store.getId(), product.getId(), quantity);
    InventoryItemDto result = assertDoesNotThrow(action);

    // Verify response DTO has expected values
    assertNotNull(result);
    assertEquals(product.getId(), result.getProduct().getId());
    assertEquals(quantity, result.getQuantity());
    assertEquals(0, result.getQuantityOnHold());

    // Verify item was added to DB and has expected values
    InventoryItem inventoryItemFromDb =
        DbTestUtils.findEntityByValue(InventoryItem.class, "id", result.getId());
    assertNotNull(inventoryItemFromDb);
    assertEquals(store.getId(), inventoryItemFromDb.getStore().getId());
    assertEquals(product.getId(), inventoryItemFromDb.getProduct().getId());
    assertEquals(quantity, inventoryItemFromDb.getQuantity());
    assertEquals(0, inventoryItemFromDb.getQuantityOnHold());
  }

  @Test
  void addQuantityTest() {
    // Authenticate with admin user
    SessionContext.setSessionContext(adminDto);

    // Run the test
    Executable action = () -> inventoryService.addQuantity(testInventoryItemDto.getId(), 3);
    assertDoesNotThrow(action);

    // Verify the quantity was added in the DB
    InventoryItem inventoryItemFromDb =
        DbTestUtils.findEntityByValue(InventoryItem.class, "id", testInventoryItemDto.getId());
    assertNotNull(inventoryItemFromDb);
    assertEquals(8, inventoryItemFromDb.getQuantity());
  }

  @Test
  void deductQuantityTest() {
    // Authenticate with admin user
    SessionContext.setSessionContext(adminDto);

    // Run the test
    Executable action = () -> inventoryService.deductQuantity(testInventoryItemDto.getId(), 3);
    assertDoesNotThrow(action);

    // Verify the quantity was deducted in the DB
    InventoryItem inventoryItemFromDb =
        DbTestUtils.findEntityByValue(InventoryItem.class, "id", testInventoryItemDto.getId());
    assertNotNull(inventoryItemFromDb);
    assertEquals(2, inventoryItemFromDb.getQuantity());
  }

  @Test
  void deductQuantity_ShouldThrow_GivenHigherAmountThanExists() {
    // Authenticate with admin user
    SessionContext.setSessionContext(adminDto);

    // Run the test
    Executable action = () -> inventoryService.deductQuantity(testInventoryItemDto.getId(), 6);
    assertThrows(InsufficientInventoryException.class, action);

    // Verify the quantity was NOT deducted in the DB and is the same as before
    InventoryItem inventoryItemFromDb =
        DbTestUtils.findEntityByValue(InventoryItem.class, "id", testInventoryItemDto.getId());
    assertNotNull(inventoryItemFromDb);
    assertEquals(5, inventoryItemFromDb.getQuantity());
  }

  @Test
  void deductQuantity_ShouldThrow_GivenHigherAmountThanAvailableAfterHold() {
    // Authenticate with admin user
    SessionContext.setSessionContext(adminDto);

    // Create inventory item with custom quantity & on-hold quantity
    InventoryItem inventoryItem = EntityTestUtils.createRandomInventoryItem(5, 3);
    DbTestUtils.persistEntity(inventoryItem);

    // Run the test
    // Attempting to deduct 3 since only 2 are available to deduct (quantity - quantityOnHold)
    Executable action = () -> inventoryService.deductQuantity(inventoryItem.getId(), 3);
    assertThrows(InsufficientInventoryException.class, action);

    // Verify the quantity was NOT deducted in the DB and is the same as before
    InventoryItem inventoryItemFromDb =
        DbTestUtils.findEntityByValue(InventoryItem.class, "id", inventoryItem.getId());
    assertNotNull(inventoryItemFromDb);
    assertEquals(5, inventoryItemFromDb.getQuantity());
    assertEquals(3, inventoryItemFromDb.getQuantityOnHold());
  }

  @Test
  void releaseQuantityTest() {
    // Authenticate with admin user
    SessionContext.setSessionContext(adminDto);

    // Create test inventory item with custom quantity & on hold quantity
    InventoryItem inventoryItem = EntityTestUtils.createRandomInventoryItem(10, 5);
    DbTestUtils.persistEntity(inventoryItem);

    // Run the test
    Executable action = () -> inventoryService.releaseQuantity(inventoryItem.getId(), 4);
    assertDoesNotThrow(action);

    // Validate quantities were updated correctly in the DB
    InventoryItem inventoryItemFromDb =
        DbTestUtils.findEntityByValue(InventoryItem.class, "id", inventoryItem.getId());
    assertNotNull(inventoryItemFromDb);
    assertEquals(6, inventoryItemFromDb.getQuantity());
    assertEquals(1, inventoryItemFromDb.getQuantityOnHold());
  }

  @Test
  void releaseQuantity_ShouldThrow_GivenHigherAmountThanOnHold() {
    // Authenticate with admin user
    SessionContext.setSessionContext(adminDto);

    // Create test inventory item with custom quantity & on hold quantity
    InventoryItem inventoryItem = EntityTestUtils.createRandomInventoryItem(10, 5);
    DbTestUtils.persistEntity(inventoryItem);

    // Run the test
    Executable action = () -> inventoryService.releaseQuantity(inventoryItem.getId(), 6);
    assertThrows(InsufficientInventoryException.class, action);

    // Verify the quantity was NOT updated in the DB
    InventoryItem inventoryItemFromDb =
        DbTestUtils.findEntityByValue(InventoryItem.class, "id", inventoryItem.getId());
    assertNotNull(inventoryItemFromDb);
    assertEquals(10, inventoryItemFromDb.getQuantity());
    assertEquals(5, inventoryItemFromDb.getQuantityOnHold());
  }

  @Test
  void releaseQuantity_ShouldThrow_GivenHigherAmountThanExists() {
    // Authenticate with admin user
    SessionContext.setSessionContext(adminDto);

    // Create test inventory item with custom quantity & on hold quantity
    InventoryItem inventoryItem = EntityTestUtils.createRandomInventoryItem(10, 5);
    DbTestUtils.persistEntity(inventoryItem);

    // Run the test
    Executable action = () -> inventoryService.releaseQuantity(inventoryItem.getId(), 11);
    assertThrows(InsufficientInventoryException.class, action);

    // Verify the quantity was NOT updated in the DB
    InventoryItem inventoryItemFromDb =
        DbTestUtils.findEntityByValue(InventoryItem.class, "id", inventoryItem.getId());
    assertNotNull(inventoryItemFromDb);
    assertEquals(10, inventoryItemFromDb.getQuantity());
    assertEquals(5, inventoryItemFromDb.getQuantityOnHold());
  }

  @Test
  void placeHoldTest() {
    // Authenticate with admin user
    SessionContext.setSessionContext(adminDto);

    // Run the test
    Executable action = () -> inventoryService.placeHold(testInventoryItemDto.getId(), 2);
    assertDoesNotThrow(action);

    // Verify the hold quantity was added in the DB
    InventoryItem inventoryItemFromDb =
        DbTestUtils.findEntityByValue(InventoryItem.class, "id", testInventoryItemDto.getId());
    assertNotNull(inventoryItemFromDb);
    assertEquals(2, inventoryItemFromDb.getQuantityOnHold());
  }

  @Test
  void placeHold_ShouldThrow_GivenHigherAmountThanAvailableToHold() {
    // Authenticate with admin user
    SessionContext.setSessionContext(adminDto);

    // Create test inventory item with custom quantity & on hold quantity
    InventoryItem inventoryItem = EntityTestUtils.createRandomInventoryItem(5, 3);
    DbTestUtils.persistEntity(inventoryItem);

    // Run the test
    Executable action = () -> inventoryService.placeHold(inventoryItem.getId(), 3);
    assertThrows(InsufficientInventoryException.class, action);

    // Verify the hold quantity was added in the DB
    InventoryItem inventoryItemFromDb =
        DbTestUtils.findEntityByValue(InventoryItem.class, "id", inventoryItem.getId());
    assertNotNull(inventoryItemFromDb);
    assertEquals(3, inventoryItemFromDb.getQuantityOnHold());
  }

  @Test
  void releaseHoldTest() {
    // Authenticate with admin user
    SessionContext.setSessionContext(adminDto);

    // Create inventory item with quantity on hold as 3 for test
    InventoryItem inventoryItem = EntityTestUtils.createRandomInventoryItem(5, 3);
    DbTestUtils.persistEntity(inventoryItem);

    // Run the test
    Executable action = () -> inventoryService.releaseHold(inventoryItem.getId(), 2);
    assertDoesNotThrow(action);

    // Verify the hold quantity was released in the DB
    InventoryItem inventoryItemFromDb =
        DbTestUtils.findEntityByValue(InventoryItem.class, "id", inventoryItem.getId());
    assertNotNull(inventoryItemFromDb);
    assertEquals(1, inventoryItemFromDb.getQuantityOnHold());
  }

  @Test
  void releaseHold_ShouldThrow_GivenHigherAmountThanAbleToRelease() {
    // Authenticate with admin user
    SessionContext.setSessionContext(adminDto);

    // Create inventory item with custom quantity & on hold quantity
    InventoryItem inventoryItem = EntityTestUtils.createRandomInventoryItem(5, 3);
    DbTestUtils.persistEntity(inventoryItem);

    // Run the test
    Executable action = () -> inventoryService.releaseHold(inventoryItem.getId(), 4);
    assertThrows(InsufficientInventoryException.class, action);

    // Verify the hold quantity was NOT released in the DB
    InventoryItem inventoryItemFromDb =
        DbTestUtils.findEntityByValue(InventoryItem.class, "id", inventoryItem.getId());
    assertNotNull(inventoryItemFromDb);
    assertEquals(3, inventoryItemFromDb.getQuantityOnHold());
  }

  @Test
  void deleteInventoryItemTest() {
    // Authenticate with admin user
    SessionContext.setSessionContext(adminDto);

    // Run the test
    Executable action = () -> inventoryService.deleteInventoryItem(testInventoryItemDto.getId());
    assertDoesNotThrow(action);

    // Verify item was deleted from the DB
    InventoryItem inventoryItemFromDb =
        DbTestUtils.findEntityByValue(InventoryItem.class, "id", testInventoryItemDto.getId());
    assertNull(inventoryItemFromDb);
  }

  @Test
  void deleteInventoryItem_ShouldThrow_WhenItemHasQuantityOnHold() {
    // Authenticate with admin user
    SessionContext.setSessionContext(adminDto);

    // Create inventory item with quantity on hold as 3 for test
    InventoryItem inventoryItem = EntityTestUtils.createRandomInventoryItem(5, 3);
    DbTestUtils.persistEntity(inventoryItem);

    // Run the test
    Executable action = () -> inventoryService.deleteInventoryItem(inventoryItem.getId());
    assertThrows(UnsafeDeletionException.class, action);
  }
}
