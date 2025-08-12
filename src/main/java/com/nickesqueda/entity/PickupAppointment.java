package com.nickesqueda.entity;

import jakarta.persistence.*;

import java.time.LocalTime;

@Entity
@Table(name = "pickup_appointments")
public class PickupAppointment extends AuditableEntity {

  @ManyToOne(optional = false)
  @JoinColumn(name = "store_id", nullable = false)
  private Store store;

  @OneToOne(optional = false)
  @JoinColumn(name = "order_id", nullable = false)
  private Order order;

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @Column(name = "start_datetime", nullable = false)
  private LocalTime startDatetime;

  @Column(name = "end_datetime", nullable = false)
  private LocalTime endDateTime;

  @Column(name = "status", nullable = false)
  private String status; // TODO: use enum
}
