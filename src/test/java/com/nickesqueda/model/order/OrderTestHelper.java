package com.nickesqueda.model.order;

import com.nickesqueda.model.DaoTestHelper;
import com.nickesqueda.testutils.EntityTestUtils;

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
