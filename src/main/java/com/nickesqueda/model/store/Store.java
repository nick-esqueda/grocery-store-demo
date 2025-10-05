package com.nickesqueda.model.store;

import com.nickesqueda.model.AuditableEntity;
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

  @Column(nullable = false, unique = true, updatable = false)
  private String address;

  @Column(name = "total_pickup_spots", nullable = false)
  private Integer totalPickupSpots;
}
