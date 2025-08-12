package com.nickesqueda;

import com.nickesqueda.dao.GenericDao;
import com.nickesqueda.entity.User;
import org.flywaydb.core.Flyway;
import org.hibernate.cfg.Configuration;

public class Main {
  public static void main(String[] args) {
    runDbMigrations();
    // SwingUtilities.invokeLater(Main::launchGui); // TODO: why is this necessary?

    GenericDao<User> userGenericDao = new GenericDao<>(User.class);
    User user = userGenericDao.findOneByValue("username", "test");

    System.out.println("User: " + user);

    if (user != null) {
      userGenericDao.delete(user);
    }

    var newUser =
        User.builder()
            .username("test")
            .email("email@email.com")
            .address("1234 something street")
            .phoneNumber("1234567890")
            .build();
    userGenericDao.save(newUser);
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
