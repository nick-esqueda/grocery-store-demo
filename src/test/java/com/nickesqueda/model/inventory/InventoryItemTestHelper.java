package com.nickesqueda.model.inventory;

import com.nickesqueda.model.DaoTestHelper;
import com.nickesqueda.testutils.EntityTestUtils;

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
