package com.nickesqueda;

import com.nickesqueda.dao.UserDao;
import com.nickesqueda.entity.User;
import javax.swing.*;
import org.flywaydb.core.Flyway;
import org.hibernate.cfg.Configuration;

public class Main {
  public static void main(String[] args) {
    runDbMigrations();
    // SwingUtilities.invokeLater(Main::launchGui); // TODO: why is this necessary?

    var dao = new UserDao();
    var user =
        User.builder()
            .username("test")
            .email("email@email.com")
            .address("1234 something street")
            .phoneNumber("1234567890")
            .build();
    dao.saveUser(user);
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
