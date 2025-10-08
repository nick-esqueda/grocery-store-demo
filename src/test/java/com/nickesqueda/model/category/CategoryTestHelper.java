package com.nickesqueda.model.category;

import com.nickesqueda.model.DaoTestHelper;
import com.nickesqueda.testutils.EntityTestUtils;

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
