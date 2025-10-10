package com.nickesqueda.grocerystoredemo.model.util;

import java.util.function.Consumer;
import lombok.Getter;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

  @Getter private static final SessionFactory sessionFactory;

  static {
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

      sessionFactory = hibernateConfig.buildSessionFactory();
    } catch (Throwable ex) {
      throw new ExceptionInInitializerError(ex);
    }
  }

  private HibernateUtil() {}

  public static void executeInTransaction(Consumer<Session> action) {
    Session session = HibernateUtil.getSessionFactory().openSession();
    Transaction transaction = null;

    // Don't use try-with-resources - session will close before rollback executes and cause failure
    try {
      transaction = session.beginTransaction();
      action.accept(session);
      transaction.commit();
    } catch (RuntimeException ex) {
      System.err.println("Exception occurred during transaction: " + ex);
      rollback(transaction);
      throw ex;
    } finally {
      session.close();
    }
  }

  private static void rollback(Transaction transaction) {
    if (transaction != null && transaction.getStatus().canRollback()) {
      try {
        transaction.rollback();
      } catch (RuntimeException rbEx) {
        System.err.println("Rollback could not be performed due to exception: " + rbEx);
      }
    } else {
      System.err.println("Transaction could not be rolled back");
    }
  }
}
