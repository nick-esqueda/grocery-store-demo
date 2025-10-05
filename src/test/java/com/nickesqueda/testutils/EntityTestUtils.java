package com.nickesqueda.testutils;

import static com.nickesqueda.testutils.TestConstants.TEST_ADDRESS;
import static com.nickesqueda.testutils.TestConstants.TEST_USERNAME;

import com.nickesqueda.model.order.Order;
import com.nickesqueda.model.order.OrderStatus;
import com.nickesqueda.model.payment.Payment;
import com.nickesqueda.model.store.Store;
import com.nickesqueda.model.user.User;
import java.math.BigDecimal;

/**
 * Contains utility methods for unit tests.
 *
 * <p>The <code>createTestX()</code> methods are necessary to avoid "detached entity" states caused
 * by reusing entities across different tests. These methods simply create an instance of an entity,
 * potentially using other entity creation methods to create <b>and persist</b> dependent entities.
 */
public final class EntityTestUtils {

  public static User createTestUser() {
    return User.builder()
        .username(TEST_USERNAME)
        .firstName("Test")
        .lastName("Test")
        .address(TEST_ADDRESS)
        .email("test@test.com")
        .phoneNumber("123-456-7890")
        .build();
  }

  public static Store createTestStore() {
    return Store.builder().address(TEST_ADDRESS).totalPickupSpots(10).build();
  }

  public static Order createTestOrder() {
    User user = createTestUser();
    Store store = createTestStore();

    DbTestUtils.persistEntity(user);
    DbTestUtils.persistEntity(store);

    return Order.builder().user(user).store(store).status(OrderStatus.PLACED).build();
  }

  public static Payment createTestPayment() {
    Order order = createTestOrder();
    DbTestUtils.persistEntity(order);

    return Payment.builder()
        .user(order.getUser())
        .order(order)
        .totalPrice(BigDecimal.ONE)
        .paymentMethodToken("1234")
        .build();
  }
}
