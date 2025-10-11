package com.nickesqueda.grocerystoredemo.model.dao;

import static org.junit.jupiter.api.Assertions.*;

import com.nickesqueda.grocerystoredemo.model.entity.*;
import com.nickesqueda.grocerystoredemo.testutils.BaseDataAccessTest;
import com.nickesqueda.grocerystoredemo.testutils.DbTestUtils;
import java.lang.reflect.InvocationTargetException;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.function.ThrowingSupplier;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DaoTest extends BaseDataAccessTest {

  @ParameterizedTest
  @ValueSource(
      classes = {
        UserTestHelper.class,
        StoreTestHelper.class,
        OrderTestHelper.class,
        PaymentTestHelper.class,
        CategoryTestHelper.class,
        ProductTestHelper.class,
        OrderItemTestHelper.class,
        CartItemTestHelper.class,
        InventoryItemTestHelper.class,
        PickupHoursTestHelper.class,
        PickupHoursAdjustmentTestHelper.class,
        PickupAppointmentTestHelper.class
      })
  <T extends BaseEntity, U> void testSave(
      Class<? extends AbstractDaoTestHelper<T, U>> entityTesterClass)
      throws NoSuchMethodException,
          InvocationTargetException,
          InstantiationException,
          IllegalAccessException {

    // Instantiate the class passed in from the @ValueSource
    AbstractDaoTestHelper<T, U> testHelper =
        entityTesterClass.getDeclaredConstructor().newInstance();

    // ARRANGE ////////////////////////////////////////////////////////////////////////////////////
    // Extract values from the test helper
    Class<T> type = testHelper.getEntityType();
    Dao<T> daoUnderTest = testHelper.getDaoUnderTest();
    T instance = testHelper.getEntity();

    // ACT ////////////////////////////////////////////////////////////////////////////////////////
    Executable action = () -> daoUnderTest.save(instance);
    assertDoesNotThrow(action);

    // ASSERT /////////////////////////////////////////////////////////////////////////////////////
    T result = DbTestUtils.findEntityByValue(type, "id", instance.getId());
    assertNotNull(result);
    assertEquals(testHelper.getExpectedAssertValue(), testHelper.getActualAssertValue(result));
  }

  @ParameterizedTest
  @ValueSource(
      classes = {
        UserTestHelper.class,
        StoreTestHelper.class,
        OrderTestHelper.class,
        PaymentTestHelper.class,
        CategoryTestHelper.class,
        ProductTestHelper.class,
        OrderItemTestHelper.class,
        CartItemTestHelper.class,
        InventoryItemTestHelper.class,
        PickupHoursTestHelper.class,
        PickupHoursAdjustmentTestHelper.class,
        PickupAppointmentTestHelper.class,
        RoleTestHelper.class
      })
  <T extends BaseEntity, U> void testFindOneByValue(
      Class<? extends AbstractDaoTestHelper<T, U>> entityTesterClass)
      throws NoSuchMethodException,
          InvocationTargetException,
          InstantiationException,
          IllegalAccessException {

    // Instantiate the class passed in from the @ValueSource
    AbstractDaoTestHelper<T, U> testHelper =
        entityTesterClass.getDeclaredConstructor().newInstance();

    // ARRANGE ////////////////////////////////////////////////////////////////////////////////////
    // Extract values from the test helper
    Dao<T> daoUnderTest = testHelper.getDaoUnderTest();
    T instance = testHelper.getEntity();

    DbTestUtils.persistEntity(instance);

    // ACT ////////////////////////////////////////////////////////////////////////////////////////
    ThrowingSupplier<T> action = () -> daoUnderTest.findOneByValue("id", instance.getId());
    T result = assertDoesNotThrow(action);

    // ASSERT /////////////////////////////////////////////////////////////////////////////////////
    assertNotNull(result);
    assertEquals(testHelper.getExpectedAssertValue(), testHelper.getActualAssertValue(result));
  }

  @ParameterizedTest
  @ValueSource(
      classes = {
        UserTestHelper.class,
        StoreTestHelper.class,
        OrderTestHelper.class,
        PaymentTestHelper.class,
        CategoryTestHelper.class,
        ProductTestHelper.class,
        OrderItemTestHelper.class,
        CartItemTestHelper.class,
        InventoryItemTestHelper.class,
        PickupHoursTestHelper.class,
        PickupHoursAdjustmentTestHelper.class,
        PickupAppointmentTestHelper.class
      })
  <T extends BaseEntity, U> void testUpdate(
      Class<? extends AbstractDaoTestHelper<T, U>> entityTesterClass)
      throws NoSuchMethodException,
          InvocationTargetException,
          InstantiationException,
          IllegalAccessException {

    // Instantiate the class passed in from the @ValueSource
    AbstractDaoTestHelper<T, U> testHelper =
        entityTesterClass.getDeclaredConstructor().newInstance();

    // ARRANGE ////////////////////////////////////////////////////////////////////////////////////
    // Extract values from the test helper
    Class<T> type = testHelper.getEntityType();
    Dao<T> daoUnderTest = testHelper.getDaoUnderTest();
    T instance = testHelper.getEntity();

    DbTestUtils.persistEntity(instance);

    // ACT ////////////////////////////////////////////////////////////////////////////////////////
    testHelper.runEntityUpdate();
    Executable action = () -> daoUnderTest.update(instance);
    assertDoesNotThrow(action);

    // ASSERT /////////////////////////////////////////////////////////////////////////////////////
    T result = DbTestUtils.findEntityByValue(type, "id", instance.getId());
    assertNotNull(result);
    assertEquals(testHelper.getExpectedAssertValue(), testHelper.getActualAssertValue(result));
  }

  @ParameterizedTest
  @ValueSource(
      classes = {
        UserTestHelper.class,
        StoreTestHelper.class,
        OrderTestHelper.class,
        PaymentTestHelper.class,
        CategoryTestHelper.class,
        ProductTestHelper.class,
        OrderItemTestHelper.class,
        CartItemTestHelper.class,
        InventoryItemTestHelper.class,
        PickupHoursTestHelper.class,
        PickupHoursAdjustmentTestHelper.class,
        PickupAppointmentTestHelper.class
      })
  <T extends BaseEntity, U> void testDelete(
      Class<? extends AbstractDaoTestHelper<T, U>> entityTesterClass)
      throws NoSuchMethodException,
          InvocationTargetException,
          InstantiationException,
          IllegalAccessException {

    // Instantiate the class passed in from the @ValueSource
    AbstractDaoTestHelper<T, U> testHelper =
        entityTesterClass.getDeclaredConstructor().newInstance();

    // ARRANGE ////////////////////////////////////////////////////////////////////////////////////
    // Extract values from the test helper
    Class<T> type = testHelper.getEntityType();
    Dao<T> daoUnderTest = testHelper.getDaoUnderTest();
    T instance = testHelper.getEntity();

    DbTestUtils.persistEntity(instance);

    // ACT ////////////////////////////////////////////////////////////////////////////////////////
    Executable action = () -> daoUnderTest.delete(instance);
    assertDoesNotThrow(action);

    // ASSERT /////////////////////////////////////////////////////////////////////////////////////
    T result = DbTestUtils.findEntityByValue(type, "id", instance.getId());
    assertNull(result);
  }
}
