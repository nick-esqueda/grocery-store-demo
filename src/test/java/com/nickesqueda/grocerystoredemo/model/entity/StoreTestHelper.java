package com.nickesqueda.grocerystoredemo.model.entity;

import com.nickesqueda.grocerystoredemo.model.dao.AbstractDaoTestHelper;
import com.nickesqueda.grocerystoredemo.testutils.EntityTestUtils;

public class StoreTestHelper extends AbstractDaoTestHelper<Store, Integer> {

  public StoreTestHelper() {
    super(Store.class, EntityTestUtils.createRandomStore());
  }

  @Override
  public void runEntityUpdate() {
    entity.setTotalPickupSpots(20);
  }

  @Override
  public Integer getExpectedAssertValue() {
    return entity.getTotalPickupSpots();
  }

  @Override
  public Integer getActualAssertValue(Store result) {
    return result.getTotalPickupSpots();
  }
}
