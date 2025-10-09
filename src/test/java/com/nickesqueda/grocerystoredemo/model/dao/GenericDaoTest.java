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

class GenericDaoTest extends BaseDataAccessTest {

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
  <T extends BaseEntity, U> void testSave(Class<? extends DaoTestHelper<T, U>> entityTesterClass)
      throws NoSuchMethodException,
          InvocationTargetException,
          InstantiationException,
          IllegalAccessException {

    // Instantiate the class passed in from the @ValueSource
    DaoTestHelper<T, U> daoTestHelper = entityTesterClass.getDeclaredConstructor().newInstance();

    // ARRANGE ////////////////////////////////////////////////////////////////////////////////////
    // Extract values from the test helper
    Class<T> type = daoTestHelper.getEntityType();
    GenericDao<T> genericDao = daoTestHelper.getGenericDao();
    T instance = daoTestHelper.getEntity();

    // ACT ////////////////////////////////////////////////////////////////////////////////////////
    Executable action = () -> genericDao.save(instance);
    assertDoesNotThrow(action);

    // ASSERT /////////////////////////////////////////////////////////////////////////////////////
    T result = DbTestUtils.findEntityByValue(type, "id", instance.getId());
    assertNotNull(result);
    assertEquals(
        daoTestHelper.getExpectedAssertValue(), daoTestHelper.getActualAssertValue(result));
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
  <T extends BaseEntity, U> void testFindOneByValue(
      Class<? extends DaoTestHelper<T, U>> entityTesterClass)
      throws NoSuchMethodException,
          InvocationTargetException,
          InstantiationException,
          IllegalAccessException {

    // Instantiate the class passed in from the @ValueSource
    DaoTestHelper<T, U> daoTestHelper = entityTesterClass.getDeclaredConstructor().newInstance();

    // ARRANGE ////////////////////////////////////////////////////////////////////////////////////
    // Extract values from the test helper
    GenericDao<T> genericDao = daoTestHelper.getGenericDao();
    T instance = daoTestHelper.getEntity();

    DbTestUtils.persistEntity(instance);

    // ACT ////////////////////////////////////////////////////////////////////////////////////////
    ThrowingSupplier<T> action = () -> genericDao.findOneByValue("id", instance.getId());
    T result = assertDoesNotThrow(action);

    // ASSERT /////////////////////////////////////////////////////////////////////////////////////
    assertNotNull(result);
    assertEquals(
        daoTestHelper.getExpectedAssertValue(), daoTestHelper.getActualAssertValue(result));
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
  <T extends BaseEntity, U> void testUpdate(Class<? extends DaoTestHelper<T, U>> entityTesterClass)
      throws NoSuchMethodException,
          InvocationTargetException,
          InstantiationException,
          IllegalAccessException {

    // Instantiate the class passed in from the @ValueSource
    DaoTestHelper<T, U> daoTestHelper = entityTesterClass.getDeclaredConstructor().newInstance();

    // ARRANGE ////////////////////////////////////////////////////////////////////////////////////
    // Extract values from the test helper
    Class<T> type = daoTestHelper.getEntityType();
    GenericDao<T> genericDao = daoTestHelper.getGenericDao();
    T instance = daoTestHelper.getEntity();

    DbTestUtils.persistEntity(instance);

    // ACT ////////////////////////////////////////////////////////////////////////////////////////
    daoTestHelper.runEntityUpdate();
    Executable action = () -> genericDao.update(instance);
    assertDoesNotThrow(action);

    // ASSERT /////////////////////////////////////////////////////////////////////////////////////
    T result = DbTestUtils.findEntityByValue(type, "id", instance.getId());
    assertNotNull(result);
    assertEquals(
        daoTestHelper.getExpectedAssertValue(), daoTestHelper.getActualAssertValue(result));
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
  <T extends BaseEntity, U> void testDelete(Class<? extends DaoTestHelper<T, U>> entityTesterClass)
      throws NoSuchMethodException,
          InvocationTargetException,
          InstantiationException,
          IllegalAccessException {

    // Instantiate the class passed in from the @ValueSource
    DaoTestHelper<T, U> daoTestHelper = entityTesterClass.getDeclaredConstructor().newInstance();

    // ARRANGE ////////////////////////////////////////////////////////////////////////////////////
    // Extract values from the test helper
    Class<T> type = daoTestHelper.getEntityType();
    GenericDao<T> genericDao = daoTestHelper.getGenericDao();
    T instance = daoTestHelper.getEntity();

    DbTestUtils.persistEntity(instance);

    // ACT ////////////////////////////////////////////////////////////////////////////////////////
    Executable action = () -> genericDao.delete(instance);
    assertDoesNotThrow(action);

    // ASSERT /////////////////////////////////////////////////////////////////////////////////////
    T result = DbTestUtils.findEntityByValue(type, "id", instance.getId());
    assertNull(result);
  }
}
