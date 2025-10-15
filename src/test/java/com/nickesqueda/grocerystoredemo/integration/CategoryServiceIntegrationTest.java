package com.nickesqueda.grocerystoredemo.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.nickesqueda.grocerystoredemo.dto.CategoryDto;
import com.nickesqueda.grocerystoredemo.dto.CategoryWithProductsDto;
import com.nickesqueda.grocerystoredemo.dto.UserDto;
import com.nickesqueda.grocerystoredemo.exception.UnauthorizedException;
import com.nickesqueda.grocerystoredemo.model.dao.Dao;
import com.nickesqueda.grocerystoredemo.model.entity.Category;
import com.nickesqueda.grocerystoredemo.model.entity.Product;
import com.nickesqueda.grocerystoredemo.model.entity.User;
import com.nickesqueda.grocerystoredemo.security.SessionContext;
import com.nickesqueda.grocerystoredemo.service.CategoryService;
import com.nickesqueda.grocerystoredemo.testutils.BaseDataAccessTest;
import com.nickesqueda.grocerystoredemo.testutils.DbTestUtils;
import com.nickesqueda.grocerystoredemo.testutils.EntityTestUtils;
import com.nickesqueda.grocerystoredemo.util.ModelMapperUtil;
import java.util.List;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.function.ThrowingSupplier;

public class CategoryServiceIntegrationTest extends BaseDataAccessTest {

  private static CategoryService categoryService;
  private CategoryDto testCategoryDto;
  private UserDto adminDto;
  private UserDto customerDto;

  @BeforeAll
  static void setUp() {
    Dao<Category> categoryDao = new Dao<>(Category.class);
    categoryService = new CategoryService(categoryDao);
  }

  @BeforeEach
  void createAdminUser() {
    User adminUser = EntityTestUtils.createRandomAdminUser();
    DbTestUtils.persistEntity(adminUser);
    this.adminDto = ModelMapperUtil.map(adminUser, UserDto.class);
  }

  @BeforeEach
  void createCustomerUser() {
    User customer = EntityTestUtils.createRandomUser();
    DbTestUtils.persistEntity(customer);
    this.customerDto = ModelMapperUtil.map(customer, UserDto.class);
  }

  @BeforeEach
  void createTestCategory() {
    Category category = EntityTestUtils.createRandomCategory();
    DbTestUtils.persistEntity(category);
    this.testCategoryDto = ModelMapperUtil.map(category, CategoryDto.class);
  }

  @AfterEach
  void tearDown() {
    SessionContext.clearSession();
  }

  @Test
  void getCategory() {
    // No auth required
    CategoryDto result = categoryService.getCategory(testCategoryDto.getId());
    assertEquals(testCategoryDto, result);
  }

  @Test
  void getCategoryWithProducts() {
    // Create multiple products associated with the test category
    Product product1 = EntityTestUtils.createRandomProduct(testCategoryDto.getId());
    Product product2 = EntityTestUtils.createRandomProduct(testCategoryDto.getId());
    Product product3 = EntityTestUtils.createRandomProduct(testCategoryDto.getId());
    DbTestUtils.persistEntity(product1);
    DbTestUtils.persistEntity(product2);
    DbTestUtils.persistEntity(product3);

    ThrowingSupplier<CategoryWithProductsDto> action =
        () -> categoryService.getCategoryWithProducts(testCategoryDto.getId());
    CategoryWithProductsDto result = assertDoesNotThrow(action);

    assertNotNull(result);
    assertNotNull(result.getProducts());
    assertEquals(3, result.getProducts().size());
  }

  @Test
  void getAllCategories() {
    // No auth required
    List<CategoryDto> result = categoryService.getAllCategories();
    assertNotNull(result);
    assertNotEquals(0, result.size());
  }

  @Test
  void createCategory() {
    // Authenticate with admin user
    SessionContext.setSessionContext(adminDto);

    // Create input
    CategoryDto newCategoryDto = CategoryDto.builder().name("test").description("test").build();

    // Run the test
    ThrowingSupplier<CategoryDto> action = () -> categoryService.createCategory(newCategoryDto);
    CategoryDto result = assertDoesNotThrow(action);

    // Verify returned DTO has the new ID and proper field values
    assertNotNull(result.getId());
    assertEquals(newCategoryDto.getName(), result.getName());
    assertEquals(newCategoryDto.getDescription(), result.getDescription());

    // Verify the DB contains the newly created category
    Category newCategoryFromDb =
        DbTestUtils.findEntityByValue(Category.class, "id", result.getId());
    assertNotNull(newCategoryFromDb);
    assertEquals(newCategoryFromDb.getName(), newCategoryDto.getName());
    assertEquals(newCategoryFromDb.getDescription(), newCategoryDto.getDescription());
  }

  @Test
  void createCategory_ShouldOnlyAllowAdminRole() {
    // Authenticate with admin user
    SessionContext.setSessionContext(customerDto);

    // Create input
    CategoryDto newCategoryDto = CategoryDto.builder().name("test").description("test").build();

    // Run the test
    Executable action = () -> categoryService.createCategory(newCategoryDto);
    assertThrows(UnauthorizedException.class, action);
  }

  @Test
  void updateCategory() {
    // Authenticate with admin user
    SessionContext.setSessionContext(adminDto);

    // Create input
    String newDescription = "updated description";
    testCategoryDto.setDescription(newDescription);

    // Run the test
    Executable action = () -> categoryService.updateCategory(testCategoryDto);
    assertDoesNotThrow(action);

    // Validate category values were update in the database
    Category categoryFromDb =
        DbTestUtils.findEntityByValue(Category.class, "id", testCategoryDto.getId());
    assertNotNull(categoryFromDb);
    assertEquals(testCategoryDto.getName(), categoryFromDb.getName());
    assertEquals(newDescription, categoryFromDb.getDescription());
  }

  @Test
  void updateCategory_ShouldOnlyAllowAdminRole() {
    // Authenticate with customer user
    SessionContext.setSessionContext(customerDto);

    // Create input
    String newDescription = "updated description";
    testCategoryDto.setDescription(newDescription);

    // Run the test
    Executable action = () -> categoryService.updateCategory(testCategoryDto);
    assertThrows(UnauthorizedException.class, action);
  }

  @Test
  void deleteCategory() {
    // Authenticate with admin user
    SessionContext.setSessionContext(adminDto);

    // Run the test
    Integer id = testCategoryDto.getId();
    Executable action = () -> categoryService.deleteCategory(id);
    assertDoesNotThrow(action);

    // Validate category was removed from the database
    Category categoryFromDb = DbTestUtils.findEntityByValue(Category.class, "id", id);
    assertNull(categoryFromDb);
  }

  @Test
  void deleteCategory_ShouldOnlyAllowAdminRole() {
    // Authenticate with customer user
    SessionContext.setSessionContext(customerDto);

    // Run the test
    Integer id = testCategoryDto.getId();
    Executable action = () -> categoryService.deleteCategory(id);
    assertThrows(UnauthorizedException.class, action);
  }
}
