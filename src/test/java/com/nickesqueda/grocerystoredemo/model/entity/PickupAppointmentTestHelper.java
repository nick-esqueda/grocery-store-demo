package com.nickesqueda.grocerystoredemo.model.entity;

import com.nickesqueda.grocerystoredemo.model.dao.DaoTestHelper;
import com.nickesqueda.grocerystoredemo.testutils.EntityTestUtils;

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
