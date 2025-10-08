package com.nickesqueda.model.pickup;

import com.nickesqueda.model.DaoTestHelper;
import com.nickesqueda.testutils.EntityTestUtils;
import java.time.LocalTime;

public class PickupHoursTestHelper extends DaoTestHelper<PickupHours, LocalTime> {

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
