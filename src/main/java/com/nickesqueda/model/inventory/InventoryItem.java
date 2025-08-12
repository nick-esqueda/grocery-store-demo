package com.nickesqueda.model.inventory;

import com.nickesqueda.model.AuditableEntity;
import com.nickesqueda.model.product.Product;
import com.nickesqueda.model.store.Store;
import jakarta.persistence.*;

@Entity
@Table(name = "inventory_items")
public class InventoryItem extends AuditableEntity {

  @ManyToOne(optional = false)
  @JoinColumn(name = "store_id", nullable = false)
  private Store store;

  @ManyToOne(optional = false)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  @Column(name = "quantity_on_hold", nullable = false)
  private Integer quantityOnHold;
}
