package com.nickesqueda.model.store;

import static com.nickesqueda.testutils.DbTestUtils.findEntityByValue;
import static com.nickesqueda.testutils.DbTestUtils.persistEntity;
import static com.nickesqueda.testutils.EntityTestUtils.createTestStore;
import static com.nickesqueda.testutils.TestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

import com.nickesqueda.model.GenericDao;
import com.nickesqueda.testutils.BaseDaoTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.ThrowingSupplier;

class StoreGenericDaoTest extends BaseDaoTest {

  private final GenericDao<Store> storeGenericDao = new GenericDao<>(Store.class);

  @Test
  void testSave() {
    assertDoesNotThrow(() -> storeGenericDao.save(createTestStore()));

    Store result = findEntityByValue(Store.class, "address", TEST_ADDRESS);
    assertNotNull(result);
    assertEquals(TEST_ADDRESS, result.getAddress());
  }

  @Test
  void testFindOneByValue() {
    persistEntity(createTestStore());

    ThrowingSupplier<Store> action = () -> storeGenericDao.findOneByValue("address", TEST_ADDRESS);

    Store result = assertDoesNotThrow(action);
    assertNotNull(result);
    assertEquals(TEST_ADDRESS, result.getAddress());
  }

  @Test
  void testUpdate() {
    Store store = createTestStore();
    persistEntity(store);
    int newTotalPickupSpots = 15;

    store.setTotalPickupSpots(newTotalPickupSpots);
    assertDoesNotThrow(() -> storeGenericDao.update(store));

    Store result = findEntityByValue(Store.class, "address", TEST_ADDRESS);
    assertNotNull(result);
    assertEquals(newTotalPickupSpots, result.getTotalPickupSpots());
  }

  @Test
  void testDelete() {
    Store store = createTestStore();
    persistEntity(store);

    assertDoesNotThrow(() -> storeGenericDao.delete(store));

    Store result = findEntityByValue(Store.class, "address", TEST_ADDRESS);
    assertNull(result);
  }
}
