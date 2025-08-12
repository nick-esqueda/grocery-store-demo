package com.nickesqueda.model.category;

import com.nickesqueda.model.AuditableEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "categories")
public class Category extends AuditableEntity {

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false)
  private String description;
}
