package com.nickesqueda.security;

import com.nickesqueda.model.BaseEntity;
import com.nickesqueda.model.user.User;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

  @Enumerated(EnumType.STRING)
  @Column(name = "name", length = 50, nullable = false, unique = true)
  private RoleName name;

  @ManyToMany(mappedBy = "roles")
  private Set<User> users = new HashSet<>();
}
