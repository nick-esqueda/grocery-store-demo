package com.nickesqueda.grocerystoredemo.model.entity;

import com.nickesqueda.grocerystoredemo.model.dao.AbstractDaoTestHelper;
import com.nickesqueda.grocerystoredemo.testutils.DbTestUtils;

public class RoleTestHelper extends AbstractDaoTestHelper<Role, RoleName> {

  public RoleTestHelper() {
    // NOTE: `DbTestUtils.persistEntity(instance)` will fail gracefully when called with below
    // entity, because `session.persist()` cannot be called with an existing entity (see
    // DbTestUtils.persistEntity()).
    super(Role.class, DbTestUtils.findEntityByValue(Role.class, "id", 1));
  }

  @Override
  public void runEntityUpdate() {
    throw new IllegalStateException("Roles cannot/should not be updated by the application");
  }

  @Override
  public RoleName getExpectedAssertValue() {
    return entity.getName();
  }

  @Override
  public RoleName getActualAssertValue(Role result) {
    return result.getName();
  }
}
