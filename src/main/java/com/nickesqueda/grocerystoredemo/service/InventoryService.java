package com.nickesqueda.grocerystoredemo.service;

import com.nickesqueda.grocerystoredemo.dto.InventoryItemDto;
import com.nickesqueda.grocerystoredemo.exception.InsufficientInventoryException;
import com.nickesqueda.grocerystoredemo.exception.UnsafeDeletionException;
import com.nickesqueda.grocerystoredemo.model.dao.Dao;
import com.nickesqueda.grocerystoredemo.model.entity.InventoryItem;
import com.nickesqueda.grocerystoredemo.model.entity.Product;
import com.nickesqueda.grocerystoredemo.model.entity.Store;
import com.nickesqueda.grocerystoredemo.security.AuthValidator;
import com.nickesqueda.grocerystoredemo.util.ModelMapperUtil;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class InventoryService {

  private final Dao<InventoryItem> inventoryItemDao;
  private final StoreService storeService;
  private final ProductService productService;

  public InventoryItem getInventoryItemEntity(Integer id) {
    return inventoryItemDao.findOneByValue("id", id);
  }

  public InventoryItemDto getInventoryItem(Integer inventoryItemId) {
    InventoryItem inventoryItem = inventoryItemDao.findOneByValue("id", inventoryItemId);
    return ModelMapperUtil.map(inventoryItem, InventoryItemDto.class);
  }

  public List<InventoryItemDto> getStoreInventory(Integer storeId) {
    List<InventoryItem> inventoryItems = inventoryItemDao.findAllByValue("store.id", storeId);
    return inventoryItems.stream()
        .map(item -> ModelMapperUtil.map(item, InventoryItemDto.class))
        .toList();
  }

  public InventoryItemDto addInventoryItem(Integer storeId, Integer productId, Integer quantity) {
    AuthValidator.requireAdminRole();

    Store store = storeService.getStoreEntity(storeId);
    Product product = productService.getProductEntity(productId);

    var inventoryItem =
        InventoryItem.builder()
            .store(store)
            .product(product)
            .quantity(quantity)
            .quantityOnHold(0)
            .build();
    inventoryItemDao.save(inventoryItem);

    return ModelMapperUtil.map(inventoryItem, InventoryItemDto.class);
  }

  /**
   * Adds the given quantity to the inventory.
   *
   * <p>Technically, this method increments the <code>quantity</code> count for the given inventory
   * item by the given amount.
   */
  public void addQuantity(Integer inventoryItemId, Integer quantityToAdd) {
    AuthValidator.requireAdminRole();

    InventoryItem inventoryItem = inventoryItemDao.findOneByValue("id", inventoryItemId);

    Integer newQuantity = inventoryItem.getQuantity() + quantityToAdd;
    inventoryItem.setQuantity(newQuantity);

    inventoryItemDao.update(inventoryItem);
  }

  /**
   * Deducts the given quantity from the inventory, unless the given quantity is greater than the
   * available amount of that item, in which case an exception will be thrown.
   *
   * <p>Available amount = <code>item.quantity - item.quantityOnHold</code>
   *
   * <p>Technically, this method deducts the <code>quantity</code> count for the given inventory
   * item by the given amount.
   */
  public void deductQuantity(Integer inventoryItemId, Integer quantityToDeduct) {
    AuthValidator.requireAdminRole();

    InventoryItem inventoryItem = inventoryItemDao.findOneByValue("id", inventoryItemId);
    int quantity = inventoryItem.getQuantity();
    int quantityOnHold = inventoryItem.getQuantityOnHold();
    int updatedQuantity = quantity - quantityToDeduct;

    if (updatedQuantity < quantityOnHold) {
      throw new InsufficientInventoryException(
          "Inventory Item ID %d: Cannot deduct %d items from inventory - %d exist and %d are currently on hold."
              .formatted(inventoryItemId, quantityToDeduct, quantity, quantityOnHold));
    }

    inventoryItem.setQuantity(updatedQuantity);
    inventoryItemDao.update(inventoryItem);
  }

  /**
   * Removes the hold and deducts the given amount of items from the inventory. One use-case is to
   * update inventory counts after a customer has ordered and picked up their items.
   *
   * <p>Technically, this method decrements the <code>quantity</code> and <code>quantityOnHold
   * </code> count for the given inventory item by the given quantity.
   */
  public void releaseQuantity(Integer inventoryItemId, Integer quantityToRelease) {
    InventoryItem inventoryItem = inventoryItemDao.findOneByValue("id", inventoryItemId);
    int quantity = inventoryItem.getQuantity();
    int quantityOnHold = inventoryItem.getQuantityOnHold();
    int updatedQuantity = quantity - quantityToRelease;
    int updatedOnHoldQuantity = quantityOnHold - quantityToRelease;

    if (updatedOnHoldQuantity < 0) {
      throw new InsufficientInventoryException(
          "Inventory Item ID %d: Cannot release hold on %d items - only %d are on hold."
              .formatted(inventoryItemId, quantityToRelease, quantityOnHold));
    } else if (updatedQuantity < 0) {
      throw new InsufficientInventoryException(
          "Inventory Item ID %d: Cannot deduct %d items - only %d exist."
              .formatted(inventoryItemId, quantityToRelease, quantity));
    }

    inventoryItem.setQuantity(updatedQuantity);
    inventoryItem.setQuantityOnHold(updatedOnHoldQuantity);
    inventoryItemDao.update(inventoryItem);
  }

  /**
   * Places a hold on the given number of inventory items. One use-case is to place a hold on items
   * being ordered by one customer so that they do not get sold to other customers while the order
   * is pending.
   *
   * <p>Technically, this method increments the <code>quantityOnHold</code> count for the given
   * inventory item by the given quantity.
   */
  public void placeHold(Integer inventoryItemId, Integer quantityToHold) {
    InventoryItem inventoryItem = inventoryItemDao.findOneByValue("id", inventoryItemId);
    int quantity = inventoryItem.getQuantity();
    int quantityOnHold = inventoryItem.getQuantityOnHold();
    int newOnHoldQuantity = inventoryItem.getQuantityOnHold() + quantityToHold;

    if (newOnHoldQuantity > quantity) {
      throw new InsufficientInventoryException(
          "Inventory Item ID %d: Cannot place hold on %d items - %d items exist and %d are currently on hold."
              .formatted(inventoryItemId, quantityToHold, quantity, quantityOnHold));
    }

    inventoryItem.setQuantityOnHold(newOnHoldQuantity);
    inventoryItemDao.update(inventoryItem);
  }

  /**
   * Removes the hold on the given number of inventory items. One use-case is to release the hold on
   * items that were ordered but never picked up, effectively making them available to other
   * customers.
   *
   * <p>Technically, this method decrements the <code>quantityOnHold</code> count for the given
   * inventory item by the given quantity.
   */
  public void releaseHold(Integer inventoryItemId, Integer quantityToRelease) {
    InventoryItem inventoryItem = inventoryItemDao.findOneByValue("id", inventoryItemId);
    int quantityOnHold = inventoryItem.getQuantityOnHold();
    int updatedOnHoldQuantity = inventoryItem.getQuantityOnHold() - quantityToRelease;

    if (updatedOnHoldQuantity < 0) {
      throw new InsufficientInventoryException(
          "Inventory Item ID %d: Cannot release hold on %d items - only %d are currently on hold."
              .formatted(inventoryItemId, quantityToRelease, quantityOnHold));
    }

    inventoryItem.setQuantityOnHold(updatedOnHoldQuantity);
    inventoryItemDao.update(inventoryItem);
  }

  public void deleteInventoryItem(Integer inventoryItemId) {
    AuthValidator.requireAdminRole();

    InventoryItem inventoryItem = inventoryItemDao.findOneByValue("id", inventoryItemId);
    int quantityOnHold = inventoryItem.getQuantityOnHold();

    if (quantityOnHold > 0) {
      throw new UnsafeDeletionException(
          "Inventory Item ID %d: Cannot be deleted - %d items are currently on hold."
              .formatted(inventoryItemId, quantityOnHold));
    }

    inventoryItemDao.delete(inventoryItem);
  }
}
