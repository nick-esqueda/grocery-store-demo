package com.nickesqueda.grocerystoredemo.model.entity;

import com.nickesqueda.grocerystoredemo.model.dao.AbstractDaoTestHelper;
import com.nickesqueda.grocerystoredemo.testutils.EntityTestUtils;

public class UserTestHelper extends AbstractDaoTestHelper<User, String> {

  public UserTestHelper() {
    super(User.class, EntityTestUtils.createRandomUser());
  }

  @Override
  public void runEntityUpdate() {
    entity.setUsername("updated-username");
  }

  @Override
  public String getExpectedAssertValue() {
    return entity.getUsername();
  }

  @Override
  public String getActualAssertValue(User result) {
    return result.getUsername();
  }
}
