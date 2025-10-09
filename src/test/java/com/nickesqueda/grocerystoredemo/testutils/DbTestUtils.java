package com.nickesqueda.grocerystoredemo.testutils;

import static com.nickesqueda.grocerystoredemo.testutils.SqlTestQuery.*;

import com.nickesqueda.grocerystoredemo.model.util.HibernateUtil;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.Transaction;

public final class DbTestUtils {

  public static void resetDbState() {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      Transaction transaction = session.beginTransaction();

      session.createNativeMutationQuery(DISABLE_FOREIGN_KEY_CHECKS).executeUpdate();

      List<String> tableNames =
          session.createNativeQuery(SELECT_ALL_TABLES, String.class).getResultList();
      tableNames.forEach(
          tableName ->
              session
                  .createNativeMutationQuery(STR_FMT_TRUNCATE_TABLE.formatted(tableName))
                  .executeUpdate());

      session.createNativeMutationQuery(ENABLE_FOREIGN_KEY_CHECKS).executeUpdate();

      transaction.commit();
    }
  }

  public static <T> void persistEntity(T entity) {
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      Transaction transaction = session.beginTransaction();
      session.persist(entity);
      transaction.commit();
    } catch (Exception ex) {
      System.err.println(
          "Test exception during persistEntity() - Entity not saved. Exception: " + ex);
    }
  }

  public static <T, U> T findEntityByValue(Class<T> entityClass, String columnName, U value) {
    String query = "FROM " + entityClass.getSimpleName() + " e WHERE e." + columnName + " = :value";

    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      return session.createQuery(query, entityClass).setParameter("value", value).getSingleResult();
    } catch (Exception ex) {
      System.err.println(
          "Test exception in findEntityByValue() - Returning null. Exception: " + ex);
      return null;
    }
  }
}
