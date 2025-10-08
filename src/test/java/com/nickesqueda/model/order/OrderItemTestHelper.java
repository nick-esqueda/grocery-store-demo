package com.nickesqueda.model.order;

import com.nickesqueda.model.DaoTestHelper;
import com.nickesqueda.testutils.EntityTestUtils;

public class OrderItemTestHelper extends DaoTestHelper<OrderItem, Integer> {

  public OrderItemTestHelper() {
    super(OrderItem.class, EntityTestUtils.createRandomOrderItem());
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
  public Integer getActualAssertValue(OrderItem result) {
    return result.getQuantity();
  }
}
