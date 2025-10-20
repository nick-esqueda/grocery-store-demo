package com.nickesqueda.grocerystoredemo.integration.crosscutting;

import static org.junit.jupiter.api.Assertions.*;

import com.nickesqueda.grocerystoredemo.dto.CategoryDto;
import com.nickesqueda.grocerystoredemo.dto.ProductDto;
import com.nickesqueda.grocerystoredemo.dto.UserCredentialsDto;
import com.nickesqueda.grocerystoredemo.dto.UserDto;
import com.nickesqueda.grocerystoredemo.exception.NoAuthRequiredException;
import com.nickesqueda.grocerystoredemo.exception.UnauthorizedException;
import com.nickesqueda.grocerystoredemo.model.dao.Dao;
import com.nickesqueda.grocerystoredemo.model.dao.ReadOnlyDao;
import com.nickesqueda.grocerystoredemo.model.entity.Category;
import com.nickesqueda.grocerystoredemo.model.entity.Product;
import com.nickesqueda.grocerystoredemo.model.entity.Role;
import com.nickesqueda.grocerystoredemo.model.entity.User;
import com.nickesqueda.grocerystoredemo.security.SessionContext;
import com.nickesqueda.grocerystoredemo.service.AuthService;
import com.nickesqueda.grocerystoredemo.service.CategoryService;
import com.nickesqueda.grocerystoredemo.service.ProductService;
import com.nickesqueda.grocerystoredemo.testutils.BaseDataAccessTest;
import com.nickesqueda.grocerystoredemo.testutils.DbTestUtils;
import com.nickesqueda.grocerystoredemo.testutils.EntityTestUtils;
import com.nickesqueda.grocerystoredemo.util.ModelMapperUtil;
import java.math.BigDecimal;
import java.util.UUID;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

public class AuthValidationIntegrationTest extends BaseDataAccessTest {

  private static AuthService authService;
  private static CategoryService categoryService;
  private static ProductService productService;
  private UserDto customerDto;
  private CategoryDto testCategoryDto;
  private ProductDto testProductDto;

  @BeforeAll
  static void setUp() {
    Dao<User> userDao = new Dao<>(User.class);
    ReadOnlyDao<Role> roleDao = new Dao<>(Role.class);
    authService = new AuthService(roleDao, userDao);

    Dao<Category> categoryDao = new Dao<>(Category.class);
    categoryService = new CategoryService(categoryDao);

    Dao<Product> productDao = new Dao<>(Product.class);
    productService = new ProductService(productDao, categoryDao);
  }

  @BeforeEach
  void beforeEach() {
    this.customerDto = createCustomerUser();
    this.testCategoryDto = createTestCategory();
    this.testProductDto = createTestProduct();
  }

  UserDto createCustomerUser() {
    User customer = EntityTestUtils.createRandomUser();
    DbTestUtils.persistEntity(customer);
    return ModelMapperUtil.map(customer, UserDto.class);
  }

  CategoryDto createTestCategory() {
    Category category = EntityTestUtils.createRandomCategory();
    DbTestUtils.persistEntity(category);
    return ModelMapperUtil.map(category, CategoryDto.class);
  }

  ProductDto createTestProduct() {
    Product product = EntityTestUtils.createRandomProduct();
    DbTestUtils.persistEntity(product);
    return ModelMapperUtil.map(product, ProductDto.class);
  }

  @AfterEach
  void clearSession() {
    SessionContext.clearSession();
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
  void deleteCategory_ShouldOnlyAllowAdminRole() {
    // Authenticate with customer user
    SessionContext.setSessionContext(customerDto);

    // Run the test
    Integer id = testCategoryDto.getId();
    Executable action = () -> categoryService.deleteCategory(id);
    assertThrows(UnauthorizedException.class, action);
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
  void deleteProductTest_ShouldOnlyAllowAdminRole() {
    // Authenticate with customer user
    SessionContext.setSessionContext(customerDto);

    // Run the test
    Executable action = () -> productService.deleteProduct(testProductDto.getId());
    assertThrows(UnauthorizedException.class, action);
  }
}
