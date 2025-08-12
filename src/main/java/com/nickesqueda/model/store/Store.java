package com.nickesqueda.model.store;

import com.nickesqueda.model.AuditableEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "stores")
public class Store extends AuditableEntity {

  @Column(nullable = false, unique = true)
  private String address;

  @Column(name = "total_pickup_spots", nullable = false)
  private Integer totalPickupSpots;
}