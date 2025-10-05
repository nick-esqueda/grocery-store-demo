package com.nickesqueda.util;

import lombok.Getter;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

  @Getter private static final SessionFactory sessionFactory = buildSessionFactory();

  private static SessionFactory buildSessionFactory() {
    try {
      Configuration hibernateConfig = new Configuration().configure();

      // Override Hibernate DB connection properties for unit test runs
      if (Boolean.parseBoolean(System.getProperty("isTestRun"))) {
        hibernateConfig.setProperty(
            "hibernate.connection.url", System.getProperty("hibernate.connection.url"));
        hibernateConfig.setProperty(
            "hibernate.connection.username", System.getProperty("hibernate.connection.username"));
        hibernateConfig.setProperty(
            "hibernate.connection.password", System.getProperty("hibernate.connection.password"));
      }

      return hibernateConfig.buildSessionFactory();
    } catch (Throwable ex) {
      throw new ExceptionInInitializerError(ex);
    }
  }
}
