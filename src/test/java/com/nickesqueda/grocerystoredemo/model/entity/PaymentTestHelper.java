package com.nickesqueda.grocerystoredemo.model.entity;

import com.nickesqueda.grocerystoredemo.model.dao.AbstractDaoTestHelper;
import com.nickesqueda.grocerystoredemo.testutils.EntityTestUtils;
import java.math.BigDecimal;

public class PaymentTestHelper extends AbstractDaoTestHelper<Payment, BigDecimal> {

  public PaymentTestHelper() {
    super(Payment.class, EntityTestUtils.createRandomPayment());
  }

  @Override
  public void runEntityUpdate() {
    entity.setTotalPrice(BigDecimal.valueOf(200, 2));
  }

  @Override
  public BigDecimal getExpectedAssertValue() {
    return entity.getTotalPrice();
  }

  @Override
  public BigDecimal getActualAssertValue(Payment result) {
    return result.getTotalPrice();
  }
}
