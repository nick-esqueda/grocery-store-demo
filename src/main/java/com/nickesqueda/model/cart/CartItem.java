package com.nickesqueda.model.cart;

import com.nickesqueda.model.AuditableEntity;
import com.nickesqueda.model.product.Product;
import com.nickesqueda.model.user.User;
import jakarta.persistence.*;

@Entity
@Table(name = "cart_items")
public class CartItem extends AuditableEntity {

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(optional = false)
  @JoinColumn(name = "product_id", nullable = false)
  private Product product;

  @Column(name = "quantity", nullable = false)
  private Integer quantity;
}
