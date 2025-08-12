package com.nickesqueda.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category extends AuditableEntity {

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false)
  private String description;
}
