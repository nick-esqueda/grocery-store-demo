package com.nickesqueda.grocerystoredemo.model.entity;

import jakarta.persistence.*;
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
}
