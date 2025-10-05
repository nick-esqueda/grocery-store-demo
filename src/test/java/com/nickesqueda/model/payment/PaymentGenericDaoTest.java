package com.nickesqueda.model.payment;

import static com.nickesqueda.testutils.DbTestUtils.findEntityByValue;
import static com.nickesqueda.testutils.DbTestUtils.persistEntity;
import static com.nickesqueda.testutils.EntityTestUtils.createTestPayment;
import static org.junit.jupiter.api.Assertions.*;

import com.nickesqueda.model.GenericDao;
import com.nickesqueda.testutils.BaseDaoTest;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

class PaymentGenericDaoTest extends BaseDaoTest {

  private final GenericDao<Payment> paymentGenericDao = new GenericDao<>(Payment.class);

  @Test
  void testSave() {
    Payment payment = createTestPayment();

    assertDoesNotThrow(() -> paymentGenericDao.save(payment));

    Payment result = findEntityByValue(Payment.class, "id", payment.getId());
    assertNotNull(result);
    assertEquals(BigDecimal.valueOf(100, 2), result.getTotalPrice());
  }

  @Test
  void testFindOneByValue() {
    Payment payment = createTestPayment();
    persistEntity(payment);

    ThrowingSupplier<Payment> action =
        () -> paymentGenericDao.findOneByValue("id", payment.getId());

    Payment result = assertDoesNotThrow(action);
    assertNotNull(result);
    assertEquals(BigDecimal.valueOf(100, 2), result.getTotalPrice());
  }

  @Test
  void testUpdate() {
    Payment payment = createTestPayment();
    persistEntity(payment);
    BigDecimal newTotalPrice = BigDecimal.valueOf(200, 2);

    payment.setTotalPrice(newTotalPrice);
    assertDoesNotThrow(() -> paymentGenericDao.update(payment));

    Payment result = findEntityByValue(Payment.class, "id", payment.getId());
    assertNotNull(result);
    assertEquals(newTotalPrice, result.getTotalPrice());
  }

  @Test
  void testDelete() {
    Payment payment = createTestPayment();
    persistEntity(payment);

    assertDoesNotThrow(() -> paymentGenericDao.delete(payment));

    Payment result = findEntityByValue(Payment.class, "id", payment.getId());
    assertNull(result);
  }
}
