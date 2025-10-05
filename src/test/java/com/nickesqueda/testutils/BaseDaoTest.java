package com.nickesqueda.testutils;

import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class BaseDaoTest {

  protected static MySQLContainer<?> testDb;

  static {
    testDb = new MySQLContainer<>("mysql:8.0.36");
    testDb.start();
  }

  @BeforeAll
  static void setup() {
    setDbConnectionProperties();
    runDbMigrations();
  }

  @AfterEach
  void resetDbState() {
    System.out.println("\n@AfterEach - RESETTING DB STATE ##############################");
    DbTestUtils.resetDbState();
    System.out.println("@AfterEach - COMPLETED DB RESET ##############################\n");
  }

  private static void setDbConnectionProperties() {
    // Set System properties so HibernateUtil knows to use the Testcontainer database
    // See HibernateUtil.java
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
}
