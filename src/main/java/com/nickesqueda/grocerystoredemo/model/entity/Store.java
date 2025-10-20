package com.nickesqueda.grocerystoredemo.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "stores")
public class Store extends AuditableEntity {

  @Column(nullable = false, unique = true)
  private String address;

  @Column(name = "total_pickup_spots", nullable = false)
  private Integer totalPickupSpots;
}
