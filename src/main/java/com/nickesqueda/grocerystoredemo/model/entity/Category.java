package com.nickesqueda.grocerystoredemo.model.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "categories")
public class Category extends AuditableEntity {

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false)
  private String description;

  @ToString.Exclude
  @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
  private List<Product> products;
}
