package com.nickesqueda.testutils;

import com.github.javafaker.Faker;
import com.nickesqueda.model.cart.CartItem;
import com.nickesqueda.model.category.Category;
import com.nickesqueda.model.inventory.InventoryItem;
import com.nickesqueda.model.order.Order;
import com.nickesqueda.model.order.OrderItem;
import com.nickesqueda.model.order.OrderStatus;
import com.nickesqueda.model.payment.Payment;
import com.nickesqueda.model.pickup.PickupAppointment;
import com.nickesqueda.model.pickup.PickupHours;
import com.nickesqueda.model.pickup.PickupHoursAdjustment;
import com.nickesqueda.model.pickup.PickupStatus;
import com.nickesqueda.model.product.Product;
import com.nickesqueda.model.store.Store;
import com.nickesqueda.model.user.User;
import com.nickesqueda.security.Role;
import com.nickesqueda.security.RoleName;
import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Set;
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
    Role customerRole = DbTestUtils.findEntityByValue(Role.class, "name", RoleName.ROLE_CUSTOMER);

    return User.builder()
        .username(UUID.randomUUID().toString())
        .firstName(FAKER.name().firstName())
        .lastName(FAKER.name().lastName())
        .address(FAKER.address().fullAddress())
        .email(FAKER.internet().emailAddress())
        .phoneNumber(FAKER.phoneNumber().phoneNumber())
        .roles(Set.of(customerRole))
        .build();
  }

  public static Store createRandomStore() {
    return Store.builder().address(FAKER.address().fullAddress()).totalPickupSpots(10).build();
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

  public static Category createRandomCategory() {
    return Category.builder()
        .name(FAKER.commerce().department())
        .description(FAKER.lorem().sentence())
        .build();
  }

  public static Product createRandomProduct() {
    Category category = createRandomCategory();
    DbTestUtils.persistEntity(category);

    return Product.builder()
        .category(category)
        .name(FAKER.commerce().productName())
        .description(FAKER.lorem().sentence())
        .price(BigDecimal.valueOf(100, 2))
        .build();
  }

  public static OrderItem createRandomOrderItem() {
    Order order = createRandomOrder();
    Product product = createRandomProduct();

    DbTestUtils.persistEntity(order);
    DbTestUtils.persistEntity(product);

    return OrderItem.builder()
        .order(order)
        .product(product)
        .quantity(1)
        .price(BigDecimal.valueOf(100, 2))
        .build();
  }

  public static CartItem createRandomCartItem() {
    User user = createRandomUser();
    Product product = createRandomProduct();

    DbTestUtils.persistEntity(user);
    DbTestUtils.persistEntity(product);

    return CartItem.builder().user(user).product(product).quantity(1).build();
  }

  public static InventoryItem createRandomInventoryItem() {
    Store store = createRandomStore();
    Product product = createRandomProduct();

    DbTestUtils.persistEntity(store);
    DbTestUtils.persistEntity(product);

    return InventoryItem.builder()
        .store(store)
        .product(product)
        .quantity(1)
        .quantityOnHold(1)
        .build();
  }

  public static PickupHours createRandomPickupHours() {
    Store store = createRandomStore();
    DbTestUtils.persistEntity(store);

    return PickupHours.builder()
        .store(store)
        .dayOfWeek(DayOfWeek.MONDAY)
        .startTime(LocalTime.of(8, 0))
        .endTime(LocalTime.of(17, 0))
        .build();
  }

  public static PickupHoursAdjustment createRandomPickupHoursAdjustment() {
    Store store = createRandomStore();
    DbTestUtils.persistEntity(store);

    return PickupHoursAdjustment.builder()
        .store(store)
        .startDateTime(LocalDateTime.of(2026, 1, 1, 0, 0))
        .endDateTime(LocalDateTime.of(2026, 1, 1, 23, 59))
        .isAvailable(false)
        .build();
  }

  public static PickupAppointment createRandomPickupAppointment() {
    User user = createRandomUser();
    Store store = createRandomStore();
    Order order = createRandomOrder();

    DbTestUtils.persistEntity(user);
    DbTestUtils.persistEntity(store);
    DbTestUtils.persistEntity(order);

    return PickupAppointment.builder()
        .user(user)
        .store(store)
        .order(order)
        .startDateTime(LocalDateTime.of(2026, 1, 1, 12, 0))
        .endDateTime(LocalDateTime.of(2026, 1, 1, 13, 0))
        .status(PickupStatus.PENDING)
        .build();
  }
}
