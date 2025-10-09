package com.nickesqueda.grocerystoredemo.model.entity;

import com.nickesqueda.grocerystoredemo.model.dao.DaoTestHelper;
import com.nickesqueda.grocerystoredemo.testutils.EntityTestUtils;

public class InventoryItemTestHelper extends DaoTestHelper<InventoryItem, Integer> {

  public InventoryItemTestHelper() {
    super(InventoryItem.class, EntityTestUtils.createRandomInventoryItem());
  }

  @Override
  public void runEntityUpdate() {
    entity.setQuantity(2);
  }

  @Override
  public Integer getExpectedAssertValue() {
    return entity.getQuantity();
  }

  @Override
  public Integer getActualAssertValue(InventoryItem result) {
    return result.getQuantity();
  }
}
