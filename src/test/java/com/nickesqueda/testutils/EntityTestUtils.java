package com.nickesqueda.testutils;

import com.github.javafaker.Faker;
import com.nickesqueda.model.order.Order;
import com.nickesqueda.model.order.OrderStatus;
import com.nickesqueda.model.payment.Payment;
import com.nickesqueda.model.store.Store;
import com.nickesqueda.model.user.User;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Contains utility methods for unit tests.
 *
 * <p>The <code>createTestX()</code> methods are necessary to avoid "detached entity" states caused
 * by reusing entities across different tests. These methods simply create an instance of an entity,
 * potentially using other entity creation methods to create <b>and persist</b> dependent entities.
 */
public final class EntityTestUtils {

  public static final Faker FAKER = new Faker();

  public static User createRandomUser() {
    return User.builder()
        .username(UUID.randomUUID().toString())
        .firstName(FAKER.name().firstName())
        .lastName(FAKER.name().lastName())
        .address(FAKER.address().fullAddress())
        .email(FAKER.internet().emailAddress())
        .phoneNumber(FAKER.phoneNumber().phoneNumber())
        .build();
  }

  public static Store createRandomStore() {
    return Store.builder()
        .address(FAKER.address().fullAddress())
        .totalPickupSpots(FAKER.number().numberBetween(0, 50))
        .build();
  }

  public static Order createRandomOrder() {
    User user = createRandomUser();
    Store store = createRandomStore();

    DbTestUtils.persistEntity(user);
    DbTestUtils.persistEntity(store);

    return Order.builder().user(user).store(store).status(OrderStatus.PLACED).build();
  }

  public static Payment createRandomPayment() {
    Order order = createRandomOrder();
    DbTestUtils.persistEntity(order);

    return Payment.builder()
        .user(order.getUser())
        .order(order)
        .totalPrice(BigDecimal.valueOf(100, 2))
        .paymentMethodToken(UUID.randomUUID().toString())
        .build();
  }
}
