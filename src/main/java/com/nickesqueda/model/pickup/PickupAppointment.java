package com.nickesqueda.model.pickup;

import com.nickesqueda.model.AuditableEntity;
import com.nickesqueda.model.order.Order;
import com.nickesqueda.model.store.Store;
import com.nickesqueda.model.user.User;
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

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private PickupStatus status;
}
