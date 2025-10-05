package com.nickesqueda.model.order;

import com.nickesqueda.model.AuditableEntity;
import com.nickesqueda.model.store.Store;
import com.nickesqueda.model.user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class Order extends AuditableEntity {

  @ManyToOne(optional = false)
  @JoinColumn(name = "user_id", nullable = false)
  private User user;

  @ManyToOne(optional = false)
  @JoinColumn(name = "store_id", nullable = false)
  private Store store;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 20)
  private OrderStatus status;
}
