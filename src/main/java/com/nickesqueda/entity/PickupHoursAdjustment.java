package com.nickesqueda.entity;

import com.nickesqueda.model.DayOfWeek;
import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "pickup_hours_adjustments")
public class PickupHoursAdjustment extends AuditableEntity {

  @ManyToOne(optional = false)
  @JoinColumn(name = "store_id", nullable = false)
  private Store store;

  @Column(name = "start_datetime", nullable = false)
  private LocalTime startDatetime;

  @Column(name = "end_datetime", nullable = false)
  private LocalTime endDateTime;

  @Column(name = "is_available", nullable = false)
  private Boolean isAvailable;
}
