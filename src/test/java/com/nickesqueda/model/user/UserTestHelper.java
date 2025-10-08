package com.nickesqueda.model.user;

import com.nickesqueda.model.DaoTestHelper;
import com.nickesqueda.testutils.EntityTestUtils;

public class UserTestHelper extends DaoTestHelper<User, String> {

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
