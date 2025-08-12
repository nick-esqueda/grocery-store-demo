package com.nickesqueda.model;

import com.nickesqueda.util.HibernateUtil;
import java.util.function.Consumer;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class GenericDao<T extends BaseEntity> {

  private final Class<T> entityClass;

  public GenericDao(Class<T> entityClass) {
    this.entityClass = entityClass;
  }

  public void save(T entity) {
    executeInTransaction(session -> session.persist(entity));
  }

  public void update(T entity) {
    executeInTransaction(session -> session.merge(entity));
  }

  public void delete(T entity) {
    executeInTransaction(session -> session.remove(entity));
  }

  public <U> T findOneByValue(String columnName, U value) {
    String query = "FROM " + entityClass.getSimpleName() + " e WHERE e." + columnName + " = :value";
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      return session.createQuery(query, entityClass).setParameter("value", value).getSingleResult();
    }
  }

  public void executeInTransaction(Consumer<Session> action) {
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

  private void rollback(Transaction transaction) {
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
