package com.nickesqueda.entity;

import jakarta.persistence.*;
import lombok.Builder;

@Builder
@Entity
@Table(name = "users")
public class User extends AuditableEntity {
  @Column(name = "username", nullable = false, unique = true)
  private String username;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "address", nullable = false)
  private String address;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "recent_payment_method_token")
  private String recentPaymentMethodToken;
}
