package com.nickesqueda.grocerystoredemo.model.entity;

import com.nickesqueda.grocerystoredemo.model.dao.AbstractDaoTestHelper;
import com.nickesqueda.grocerystoredemo.testutils.EntityTestUtils;

public class CartItemTestHelper extends AbstractDaoTestHelper<CartItem, Integer> {

  public CartItemTestHelper() {
    super(CartItem.class, EntityTestUtils.createRandomCartItem());
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
  public Integer getActualAssertValue(CartItem result) {
    return result.getQuantity();
  }
}
