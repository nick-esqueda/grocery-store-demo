package com.nickesqueda.grocerystoredemo.model.entity;

import com.nickesqueda.grocerystoredemo.model.dao.DaoTestHelper;
import com.nickesqueda.grocerystoredemo.testutils.EntityTestUtils;

public class OrderTestHelper extends DaoTestHelper<Order, OrderStatus> {

  public OrderTestHelper() {
    super(Order.class, EntityTestUtils.createRandomOrder());
  }

  @Override
  public void runEntityUpdate() {
    entity.setStatus(OrderStatus.COMPLETED);
  }

  @Override
  public OrderStatus getExpectedAssertValue() {
    return entity.getStatus();
  }

  @Override
  public OrderStatus getActualAssertValue(Order result) {
    return result.getStatus();
  }
}
