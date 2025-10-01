package com.nickesqueda;

import com.nickesqueda.model.GenericDao;
import com.nickesqueda.model.category.Category;
import com.nickesqueda.model.product.Product;
import org.flywaydb.core.Flyway;
import org.hibernate.cfg.Configuration;

import java.math.BigDecimal;

public class Main {
  public static void main(String[] args) {
    runDbMigrations();
    // SwingUtilities.invokeLater(Main::launchGui); // TODO: why is this necessary?

    demo();
  }

  private static void demo() {
    var categoryDao = new GenericDao<>(Category.class);
    var category =
        Category.builder().name("electronics").description("all electronic devices").build();
    categoryDao.save(category);

    var productDao = new GenericDao<>(Product.class);
    var product =
        Product.builder()
            .name("Phone")
            .description("A brand new phone")
            .price(BigDecimal.ONE)
            .category(category)
            .build();

    productDao.save(product);

    Category categoryResult = categoryDao.findOneByValue("name", "electronics");
    Product productResult = productDao.findOneByValue("name", "Phone");
    System.out.println("Category: " + categoryResult);
    System.out.println("Product: " + productResult);
  }

  private static void launchGui() {
    // TODO
  }

  private static void runDbMigrations() {
    Configuration hibernateConfig = new Configuration().configure();

    String jdbcUrl = hibernateConfig.getProperty("hibernate.connection.url");
    String username = hibernateConfig.getProperty("hibernate.connection.username");
    String password = hibernateConfig.getProperty("hibernate.connection.password");

    // Run Flyway migrations using Hibernate's DB settings
    Flyway flyway = Flyway.configure().dataSource(jdbcUrl, username, password).load();
    flyway.migrate();
  }
}
