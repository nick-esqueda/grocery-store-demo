package persistence;

import com.nickesqueda.model.GenericDao;
import com.nickesqueda.model.category.Category;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class DemoTest {

  protected static MySQLContainer<?> testDb;

  @BeforeAll
  static void setup() {
    initializeContainer();
    buildSessionFactory();
    runDbMigrations();
  }

  private static void initializeContainer() {
    MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.36");
    mySQLContainer.start();
    testDb = mySQLContainer;
  }

  private static void buildSessionFactory() {
    // Set System properties so HibernateUtil knows to use the Testcontainer database
    System.setProperty("isTestRun", "true");
    System.setProperty("hibernate.connection.url", testDb.getJdbcUrl());
    System.setProperty("hibernate.connection.username", testDb.getUsername());
    System.setProperty("hibernate.connection.password", testDb.getPassword());
  }

  private static void runDbMigrations() {
    // Run Flyway migrations on the Testcontainer database
    Flyway flyway =
        Flyway.configure()
            .dataSource(testDb.getJdbcUrl(), testDb.getUsername(), testDb.getPassword())
            .load();
    flyway.migrate();
  }

  @Test
  public void demoTest() {
    GenericDao<Category> categoryDao = new GenericDao<>(Category.class);
    Category category = Category.builder().name("test").description("test").build();
    categoryDao.save(category);

    Category actualCategory = categoryDao.findOneByValue("name", "test");

    Assertions.assertEquals("test", actualCategory.getName());
  }
}
