package com.nickesqueda.grocerystoredemo.model.entity;

import jakarta.persistence.*;
import java.util.Set;
import lombok.*;
import org.hibernate.annotations.Immutable;

@Getter
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Immutable
@Table(name = "roles")
public class Role extends BaseEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "name", length = 50, nullable = false, unique = true)
  private RoleName name;

  @ToString.Exclude
  @ManyToMany(mappedBy = "roles")
  private Set<User> users;
}
