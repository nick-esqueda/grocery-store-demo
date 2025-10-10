package com.nickesqueda.grocerystoredemo.model.entity;

import com.nickesqueda.grocerystoredemo.model.dao.AbstractDaoTestHelper;
import com.nickesqueda.grocerystoredemo.testutils.EntityTestUtils;

public class OrderItemTestHelper extends AbstractDaoTestHelper<OrderItem, Integer> {

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
