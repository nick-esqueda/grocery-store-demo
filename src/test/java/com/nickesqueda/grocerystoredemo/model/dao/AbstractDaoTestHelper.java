package com.nickesqueda.grocerystoredemo.model.dao;

import com.nickesqueda.grocerystoredemo.model.entity.BaseEntity;
import lombok.Getter;

@Getter
public abstract class AbstractDaoTestHelper<T extends BaseEntity, U> {

  protected final Class<T> entityType;
  protected final T entity;
  protected final Dao<T> daoUnderTest;

  public AbstractDaoTestHelper(Class<T> entityType, T entity) {
    this.entityType = entityType;
    this.entity = entity;
    this.daoUnderTest = new Dao<>(entityType);
  }

  /**
   * This method performs an update on an arbitrary field of type <code>U</code> inside <code>
   * entityInstance</code>, chosen & implemented by the concrete subclass.
   *
   * <p>The updated field of type <code>U</code> will be returned from <code>
   * getExpectedAssertValue()</code> and <code>
   * getActualAssertValue()</code>.
   */
  public abstract void runEntityUpdate();

  /**
   * Returns the value from the Entity instance to be used as the "expected" part of <code>
   * assertEquals(expected, actual)</code>.
   *
   * <p>This method is required as client code won't know the concrete Entity's fields, therefore
   * can't call the correct getter when trying to <code>assertEquals()</code>.
   */
  public abstract U getExpectedAssertValue();

  /**
   * Returns the value from the passed-in Entity instance to be used as the "actual" part of <code>
   * assertEquals(expected, actual)</code>.
   *
   * <p>This method is required as client code won't know the concrete Entity's fields, therefore
   * can't call the correct getter when trying to <code>assertEquals()</code>.
   */
  public abstract U getActualAssertValue(T result);
}
