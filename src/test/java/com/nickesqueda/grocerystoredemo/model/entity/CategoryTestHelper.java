package com.nickesqueda.grocerystoredemo.model.entity;

import com.nickesqueda.grocerystoredemo.model.dao.DaoTestHelper;
import com.nickesqueda.grocerystoredemo.testutils.EntityTestUtils;

public class CategoryTestHelper extends DaoTestHelper<Category, String> {

  public CategoryTestHelper() {
    super(Category.class, EntityTestUtils.createRandomCategory());
  }

  @Override
  public void runEntityUpdate() {
    entity.setName("updated-category");
  }

  @Override
  public String getExpectedAssertValue() {
    return entity.getName();
  }

  @Override
  public String getActualAssertValue(Category result) {
    return result.getName();
  }
}
