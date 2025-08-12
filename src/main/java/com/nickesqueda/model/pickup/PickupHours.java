package com.nickesqueda.model.pickup;


import com.nickesqueda.model.AuditableEntity;
import com.nickesqueda.model.store.Store;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "pickup_hours")
public class PickupHours extends AuditableEntity {

  @ManyToOne(optional = false)
  @JoinColumn(name = "store_id", nullable = false)
  private Store store;

  @Enumerated(EnumType.STRING)
  @Column(name = "day_of_week", nullable = false)
  private DayOfWeek dayOfWeek;

  @Column(name = "start_time", nullable = false)
  private LocalTime startTime;

  @Column(name = "end_time", nullable = false)
  private LocalTime endTime;
}
