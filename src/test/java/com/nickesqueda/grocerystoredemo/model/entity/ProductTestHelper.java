package com.nickesqueda.grocerystoredemo.model.entity;

import com.nickesqueda.grocerystoredemo.model.dao.DaoTestHelper;
import com.nickesqueda.grocerystoredemo.testutils.EntityTestUtils;

public class ProductTestHelper extends DaoTestHelper<Product, String> {

  public ProductTestHelper() {
    super(Product.class, EntityTestUtils.createRandomProduct());
  }

  @Override
  public void runEntityUpdate() {
    entity.setDescription("updated description");
  }

  @Override
  public String getExpectedAssertValue() {
    return entity.getDescription();
  }

  @Override
  public String getActualAssertValue(Product result) {
    return result.getDescription();
  }
}
