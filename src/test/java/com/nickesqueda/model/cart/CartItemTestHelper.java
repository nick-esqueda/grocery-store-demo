package com.nickesqueda.model.cart;

import com.nickesqueda.model.DaoTestHelper;
import com.nickesqueda.testutils.EntityTestUtils;

public class CartItemTestHelper extends DaoTestHelper<CartItem, Integer> {

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
