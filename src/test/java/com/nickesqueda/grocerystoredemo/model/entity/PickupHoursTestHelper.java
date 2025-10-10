package com.nickesqueda.grocerystoredemo.model.entity;

import com.nickesqueda.grocerystoredemo.model.dao.AbstractDaoTestHelper;
import com.nickesqueda.grocerystoredemo.testutils.EntityTestUtils;
import java.time.LocalTime;

public class PickupHoursTestHelper extends AbstractDaoTestHelper<PickupHours, LocalTime> {

  public PickupHoursTestHelper() {
    super(PickupHours.class, EntityTestUtils.createRandomPickupHours());
  }

  @Override
  public void runEntityUpdate() {
    entity.setStartTime(LocalTime.of(12, 0));
  }

  @Override
  public LocalTime getExpectedAssertValue() {
    return null;
  }

  @Override
  public LocalTime getActualAssertValue(PickupHours result) {
    return null;
  }
}
