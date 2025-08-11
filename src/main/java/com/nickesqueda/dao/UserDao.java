package com.nickesqueda.dao;

import com.nickesqueda.entity.User;
import com.nickesqueda.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserDao {

  public void saveUser(User user) {
    Transaction transaction = null;
    try (Session session = HibernateUtil.getSessionFactory().openSession()) {
      transaction = session.beginTransaction();
      session.persist(user);
      transaction.commit();
    } catch (RuntimeException e) {
      if (transaction != null && transaction.getStatus().canRollback()) {
        transaction.rollback();
      }
      throw e;
    }
  }
}
