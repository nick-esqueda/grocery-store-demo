package com.nickesqueda.model.pickup;

import com.nickesqueda.model.DaoTestHelper;
import com.nickesqueda.testutils.EntityTestUtils;
import java.time.LocalDateTime;

public class PickupHoursAdjustmentTestHelper
    extends DaoTestHelper<PickupHoursAdjustment, LocalDateTime> {

  public PickupHoursAdjustmentTestHelper() {
    super(PickupHoursAdjustment.class, EntityTestUtils.createRandomPickupHoursAdjustment());
  }

  @Override
  public void runEntityUpdate() {
    entity.setEndDateTime(LocalDateTime.of(2026, 1, 2, 23, 0));
  }

  @Override
  public LocalDateTime getExpectedAssertValue() {
    return entity.getEndDateTime();
  }

  @Override
  public LocalDateTime getActualAssertValue(PickupHoursAdjustment result) {
    return result.getEndDateTime();
  }
}
