package com.nickesqueda.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "payments")
public class Payment extends AuditableEntity {

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(optional = false)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @Column(name = "total_price", nullable = false, precision = 6, scale = 2)
  private BigDecimal totalPrice;

  @Column(name = "payment_method_token", nullable = false)
  private String paymentMethodToken;
}
