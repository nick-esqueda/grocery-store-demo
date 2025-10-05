package com.nickesqueda.model.order;

import static com.nickesqueda.testutils.DbTestUtils.findEntityByValue;
import static com.nickesqueda.testutils.DbTestUtils.persistEntity;
import static com.nickesqueda.testutils.EntityTestUtils.createTestOrder;
import static org.junit.jupiter.api.Assertions.*;

import com.nickesqueda.model.GenericDao;
import com.nickesqueda.testutils.BaseDaoTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

class OrderGenericDaoTest extends BaseDaoTest {

  private final GenericDao<Order> orderGenericDao = new GenericDao<>(Order.class);

  @Test
  void testSave() {
    Order order = createTestOrder();

    assertDoesNotThrow(() -> orderGenericDao.save(order));

    Order result = findEntityByValue(Order.class, "id", order.getId());
    assertNotNull(result);
    assertEquals(OrderStatus.PLACED, result.getStatus());
  }

  @Test
  void testFindOneByValue() {
    Order order = createTestOrder();
    persistEntity(order);

    ThrowingSupplier<Order> action = () -> orderGenericDao.findOneByValue("id", order.getId());

    Order result = assertDoesNotThrow(action);
    assertNotNull(result);
    assertEquals(OrderStatus.PLACED, result.getStatus());
  }

  @Test
  void testUpdate() {
    Order order = createTestOrder();
    persistEntity(order);
    OrderStatus newOrderStatus = OrderStatus.COMPLETED;

    order.setStatus(newOrderStatus);
    assertDoesNotThrow(() -> orderGenericDao.update(order));

    Order result = findEntityByValue(Order.class, "id", order.getId());
    assertNotNull(result);
    assertEquals(newOrderStatus, result.getStatus());
  }

  @Test
  void testDelete() {
    Order order = createTestOrder();
    persistEntity(order);

    assertDoesNotThrow(() -> orderGenericDao.delete(order));

    Order result = findEntityByValue(Order.class, "id", order.getId());
    assertNull(result);
  }
}
