package com.nickesqueda.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
public class Product extends AuditableEntity {

  @ManyToOne(optional = false)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(name = "total_price", nullable = false, precision = 6, scale = 2)
  private BigDecimal totalPrice;
}
