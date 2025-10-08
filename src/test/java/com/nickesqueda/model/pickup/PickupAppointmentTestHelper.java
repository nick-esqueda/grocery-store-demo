package com.nickesqueda.model.pickup;

import com.nickesqueda.model.DaoTestHelper;
import com.nickesqueda.testutils.EntityTestUtils;

public class PickupAppointmentTestHelper extends DaoTestHelper<PickupAppointment, PickupStatus> {

  public PickupAppointmentTestHelper() {
    super(PickupAppointment.class, EntityTestUtils.createRandomPickupAppointment());
  }

  @Override
  public void runEntityUpdate() {
    entity.setStatus(PickupStatus.COMPLETED);
  }

  @Override
  public PickupStatus getExpectedAssertValue() {
    return entity.getStatus();
  }

  @Override
  public PickupStatus getActualAssertValue(PickupAppointment result) {
    return result.getStatus();
  }
}
