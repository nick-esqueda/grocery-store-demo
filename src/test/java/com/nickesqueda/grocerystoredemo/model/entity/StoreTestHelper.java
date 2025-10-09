package com.nickesqueda.grocerystoredemo.model.entity;

import com.nickesqueda.grocerystoredemo.model.dao.DaoTestHelper;
import com.nickesqueda.grocerystoredemo.testutils.EntityTestUtils;

public class StoreTestHelper extends DaoTestHelper<Store, Integer> {

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
