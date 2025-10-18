package com.nickesqueda.grocerystoredemo.integration;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.nickesqueda.grocerystoredemo.dto.CategoryDto;
import com.nickesqueda.grocerystoredemo.dto.ProductDto;
import com.nickesqueda.grocerystoredemo.dto.UserDto;
import com.nickesqueda.grocerystoredemo.exception.UnauthorizedException;
import com.nickesqueda.grocerystoredemo.model.dao.Dao;
import com.nickesqueda.grocerystoredemo.model.entity.Category;
import com.nickesqueda.grocerystoredemo.model.entity.Product;
import com.nickesqueda.grocerystoredemo.model.entity.User;
import com.nickesqueda.grocerystoredemo.security.SessionContext;
import com.nickesqueda.grocerystoredemo.service.ProductService;
import com.nickesqueda.grocerystoredemo.testutils.BaseDataAccessTest;
import com.nickesqueda.grocerystoredemo.testutils.DbTestUtils;
import com.nickesqueda.grocerystoredemo.testutils.EntityTestUtils;
import com.nickesqueda.grocerystoredemo.util.ModelMapperUtil;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.function.ThrowingSupplier;

public class ProductServiceIntegrationTest extends BaseDataAccessTest {

  private static ProductService productService;
  private ProductDto testProductDto;
  private CategoryDto testCategoryDto;
  private UserDto adminDto;
  private UserDto customerDto;

  @BeforeAll
  static void setUp() {
    Dao<Product> productDao = new Dao<>(Product.class);
    Dao<Category> categoryDao = new Dao<>(Category.class);
    productService = new ProductService(productDao, categoryDao);
  }

  @BeforeEach
  void beforeEach() {
    this.adminDto = createAdminUser();
    this.customerDto = createCustomerUser();
    this.testProductDto = createTestProduct();
    this.testCategoryDto = createTestCategory();
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

  ProductDto createTestProduct() {
    Product product = EntityTestUtils.createRandomProduct();
    DbTestUtils.persistEntity(product);
    return ModelMapperUtil.map(product, ProductDto.class);
  }

  CategoryDto createTestCategory() {
    Category category = EntityTestUtils.createRandomCategory();
    DbTestUtils.persistEntity(category);
    return ModelMapperUtil.map(category, CategoryDto.class);
  }

  @AfterEach
  void clearSession() {
    SessionContext.clearSession();
  }

  @Test
  void getProductDetailsTest() {
    // No auth required

    ThrowingSupplier<ProductDto> action =
        () -> productService.getProductDetails(testProductDto.getId());
    ProductDto result = assertDoesNotThrow(action);

    assertEquals(testProductDto, result);
  }

  @Test
  void getProductsInCategoryTest() {
    // No auth required

    // Create multiple products associated with a new category
    Category category = EntityTestUtils.createRandomCategory();
    DbTestUtils.persistEntity(category);
    Integer categoryId = category.getId();

    Product product1 = EntityTestUtils.createRandomProduct(categoryId);
    Product product2 = EntityTestUtils.createRandomProduct(categoryId);
    Product product3 = EntityTestUtils.createRandomProduct(categoryId);
    DbTestUtils.persistEntity(product1);
    DbTestUtils.persistEntity(product2);
    DbTestUtils.persistEntity(product3);

    // Convert to DTOs for comparison
    ProductDto product1Dto = ModelMapperUtil.map(product1, ProductDto.class);
    ProductDto product2Dto = ModelMapperUtil.map(product2, ProductDto.class);
    ProductDto product3Dto = ModelMapperUtil.map(product3, ProductDto.class);

    // Run the test
    ThrowingSupplier<List<ProductDto>> action =
        () -> productService.getProductsInCategory(categoryId);
    List<ProductDto> result = assertDoesNotThrow(action);

    // Verify the list has the 3 expected items
    assertNotNull(result);
    assertEquals(3, result.size());
    assertTrue(result.stream().anyMatch(productDto -> productDto.equals(product1Dto)));
    assertTrue(result.stream().anyMatch(productDto -> productDto.equals(product2Dto)));
    assertTrue(result.stream().anyMatch(productDto -> productDto.equals(product3Dto)));
  }

  @Test
  void addProductTest() {
    // Authenticate with admin user
    SessionContext.setSessionContext(adminDto);

    // Create input
    ProductDto productDto =
        ProductDto.builder()
            .name("test")
            .description("test")
            .price(BigDecimal.valueOf(10000, 2))
            .categoryId(testCategoryDto.getId())
            .build();

    // Run the test
    ThrowingSupplier<ProductDto> action = () -> productService.addProduct(productDto);
    ProductDto result = assertDoesNotThrow(action);

    // Verify the returned result has proper values
    assertNotNull(result.getId());
    assertEquals(productDto.getName(), result.getName());
    assertEquals(productDto.getDescription(), result.getDescription());
    assertEquals(productDto.getPrice(), result.getPrice());
    assertEquals(productDto.getCategoryId(), result.getCategoryId());

    // Verify added to database
    Product productFromDb = DbTestUtils.findEntityByValue(Product.class, "id", result.getId());
    assertNotNull(productFromDb);
    assertEquals(productFromDb.getName(), result.getName());
    assertEquals(productFromDb.getDescription(), result.getDescription());
    assertEquals(productFromDb.getPrice(), result.getPrice());
    assertEquals(productFromDb.getCategory().getId(), result.getCategoryId());
  }

  @Test
  void addProductTest_ShouldOnlyAllowAdminRole() {
    // Authenticate with customer user
    SessionContext.setSessionContext(customerDto);

    // Create input
    ProductDto productDto =
        ProductDto.builder()
            .name("test")
            .description("test")
            .price(BigDecimal.valueOf(10000, 2))
            .categoryId(testCategoryDto.getId())
            .build();

    // Run the test
    Executable action = () -> productService.addProduct(productDto);
    assertThrows(UnauthorizedException.class, action);
  }

  @Test
  void updateProductDetailsTest() {
    // Authenticate with admin user
    SessionContext.setSessionContext(adminDto);

    // Create new category to associate product with
    Category newCategory = EntityTestUtils.createRandomCategory();
    DbTestUtils.persistEntity(newCategory);

    // Create input
    testProductDto.setPrice(BigDecimal.valueOf(20000, 2));
    testProductDto.setCategoryId(newCategory.getId());

    // Run the test
    Executable action = () -> productService.updateProductDetails(testProductDto);
    assertDoesNotThrow(action);

    // Verify the product in DB has the updated values
    Product productFromDb =
        DbTestUtils.findEntityByValue(Product.class, "id", testProductDto.getId());
    assertNotNull(productFromDb);
    assertEquals(testProductDto.getPrice(), productFromDb.getPrice());
    assertEquals(testProductDto.getCategoryId(), productFromDb.getCategory().getId());
  }

  @Test
  void updateProductDetailsTest_ShouldOnlyAllowAdminRole() {
    // Authenticate with admin user
    SessionContext.setSessionContext(customerDto);

    // Create input
    testProductDto.setPrice(BigDecimal.valueOf(20000, 2));

    // Run the test
    Executable action = () -> productService.updateProductDetails(testProductDto);
    assertThrows(UnauthorizedException.class, action);
  }

  @Test
  void deleteProductTest() {
    // Authenticate with admin user
    SessionContext.setSessionContext(adminDto);

    // Run the test
    Executable action = () -> productService.deleteProduct(testProductDto.getId());
    assertDoesNotThrow(action);

    // Verify product was deleted from the database
    Product productFromDb =
        DbTestUtils.findEntityByValue(Product.class, "id", testProductDto.getId());
    assertNull(productFromDb);
  }

  @Test
  void deleteProductTest_ShouldOnlyAllowAdminRole() {
    // Authenticate with customer user
    SessionContext.setSessionContext(customerDto);

    // Run the test
    Executable action = () -> productService.deleteProduct(testProductDto.getId());
    assertThrows(UnauthorizedException.class, action);
  }
}
