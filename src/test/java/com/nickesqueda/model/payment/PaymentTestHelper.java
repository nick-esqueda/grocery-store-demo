package com.nickesqueda.model.payment;

import com.nickesqueda.model.DaoTestHelper;
import com.nickesqueda.testutils.EntityTestUtils;
import java.math.BigDecimal;

public class PaymentTestHelper extends DaoTestHelper<Payment, BigDecimal> {

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
