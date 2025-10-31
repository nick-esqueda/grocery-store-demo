package com.nickesqueda.grocerystoredemo.integration.crosscutting;

import static org.junit.jupiter.api.Assertions.*;

import com.nickesqueda.grocerystoredemo.dto.*;
import com.nickesqueda.grocerystoredemo.exception.NoAuthRequiredException;
import com.nickesqueda.grocerystoredemo.exception.UnauthorizedException;
import com.nickesqueda.grocerystoredemo.model.dao.Dao;
import com.nickesqueda.grocerystoredemo.model.dao.ReadOnlyDao;
import com.nickesqueda.grocerystoredemo.model.entity.*;
import com.nickesqueda.grocerystoredemo.security.SessionContext;
import com.nickesqueda.grocerystoredemo.service.*;
import com.nickesqueda.grocerystoredemo.testutils.BaseDataAccessTest;
import com.nickesqueda.grocerystoredemo.testutils.DbTestUtils;
import com.nickesqueda.grocerystoredemo.testutils.EntityTestUtils;
import com.nickesqueda.grocerystoredemo.util.ModelMapperUtil;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

public class AuthValidationIntegrationTest extends BaseDataAccessTest {

  private static AuthService authService;
  private static CategoryService categoryService;
  private static ProductService productService;
  private static StoreService storeService;
  private static InventoryService inventoryService;
  private static CategoryDto testCategoryDto;
  private static ProductDto testProductDto;
  private static StoreDto testStoreDto;
  private static InventoryItemDto testInventoryItemDto;
  private UserDto customerDto;

  @BeforeAll
  static void setUp() {
    var roleDao = new ReadOnlyDao<>(Role.class);
    var userDao = new Dao<>(User.class);
    var categoryDao = new Dao<>(Category.class);
    var productDao = new Dao<>(Product.class);
    var storeDao = new Dao<>(Store.class);
    var inventoryItemDao = new Dao<>(InventoryItem.class);

    authService = new AuthService(roleDao, userDao);
    categoryService = new CategoryService(categoryDao);
    productService = new ProductService(productDao, categoryService);
    storeService = new StoreService(storeDao);
    inventoryService = new InventoryService(inventoryItemDao, storeService, productService);

    testCategoryDto = createTestCategory();
    testProductDto = createTestProduct();
    testStoreDto = createTestStore();
    testInventoryItemDto = createInventoryItem();
  }

  @BeforeEach
  void beforeEach() {
    this.customerDto = createCustomerUser();
  }

  UserDto createCustomerUser() {
    User customer = EntityTestUtils.createRandomUser();
    DbTestUtils.persistEntity(customer);
    return ModelMapperUtil.map(customer, UserDto.class);
  }

  static CategoryDto createTestCategory() {
    Category category = EntityTestUtils.createRandomCategory();
    DbTestUtils.persistEntity(category);
    return ModelMapperUtil.map(category, CategoryDto.class);
  }

  static ProductDto createTestProduct() {
    Product product = EntityTestUtils.createRandomProduct();
    DbTestUtils.persistEntity(product);
    return ModelMapperUtil.map(product, ProductDto.class);
  }

  static StoreDto createTestStore() {
    Store store = EntityTestUtils.createRandomStore();
    DbTestUtils.persistEntity(store);
    return ModelMapperUtil.map(store, StoreDto.class);
  }

  static InventoryItemDto createInventoryItem() {
    InventoryItem inventoryItem = EntityTestUtils.createRandomInventoryItem(5, 0);
    DbTestUtils.persistEntity(inventoryItem);
    return ModelMapperUtil.map(inventoryItem, InventoryItemDto.class);
  }

  @AfterEach
  void clearSession() {
    SessionContext.clearSession();
  }

  @ParameterizedTest
  @MethodSource("serviceMethodProvider")
  void methodShouldOnlyAllowAdminRole(Executable serviceMethod) {
    SessionContext.setSessionContext(customerDto);
    assertThrows(UnauthorizedException.class, serviceMethod);
  }

  static Stream<Executable> serviceMethodProvider() {
    // For createX() methods
    CategoryDto newCategoryDto = CategoryDto.builder().name("test").description("test").build();
    ProductDto newProductDto =
        ProductDto.builder()
            .name("test")
            .description("test")
            .price(BigDecimal.valueOf(10000, 2))
            .categoryId(testCategoryDto.getId())
            .build();
    StoreDto newStoreDto = StoreDto.builder().address("test").totalPickupSpots(20).build();

    // For updateX() methods
    testCategoryDto.setDescription("updated description");
    testProductDto.setPrice(BigDecimal.valueOf(20000, 2));
    testStoreDto.setTotalPickupSpots(20);

    return Stream.of(
        () -> categoryService.createCategory(newCategoryDto),
        () -> categoryService.updateCategory(testCategoryDto),
        () -> categoryService.deleteCategory(testCategoryDto.getId()),
        () -> productService.addProduct(newProductDto),
        () -> productService.updateProductDetails(testProductDto),
        () -> productService.deleteProduct(testProductDto.getId()),
        () -> storeService.createStore(newStoreDto),
        () -> storeService.updateStoreDetails(testStoreDto),
        () -> storeService.deleteStore(testStoreDto.getId()),
        () -> inventoryService.addInventoryItem(testStoreDto.getId(), testProductDto.getId(), 5),
        () -> inventoryService.addQuantity(testInventoryItemDto.getId(), 5),
        () -> inventoryService.deductQuantity(testInventoryItemDto.getId(), 1),
        () -> inventoryService.deleteInventoryItem(testInventoryItemDto.getId()));
  }

  @Test
  void registerUser_ShouldThrow_WhenSessionIsActive() {
    // Authenticate with customer user so that session is active
    SessionContext.setSessionContext(customerDto);

    // Create input
    UserDto userDto = EntityTestUtils.createRandomUserDto();
    String rawPassword = UUID.randomUUID().toString();

    // Run the test
    Executable action = () -> authService.registerUser(userDto, rawPassword);
    assertThrows(NoAuthRequiredException.class, action);
  }

  @Test
  void authenticateUser_ShouldThrow_WhenSessionIsActive() {
    // Authenticate with customer user so that session is active
    SessionContext.setSessionContext(customerDto);

    // Create test user with custom password
    String rawPassword = UUID.randomUUID().toString();
    User testUser = EntityTestUtils.createRandomUser(rawPassword);
    DbTestUtils.persistEntity(testUser);

    // Create input
    var credentials = new UserCredentialsDto(testUser.getUsername(), rawPassword);

    // Run the test
    Executable action = () -> authService.authenticateUser(credentials);
    assertThrows(NoAuthRequiredException.class, action);
  }
}
