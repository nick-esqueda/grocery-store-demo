package com.nickesqueda.model.product;

import com.nickesqueda.model.AuditableEntity;
import com.nickesqueda.model.category.Category;
import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.*;

@Getter
@Setter
@ToString(callSuper = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class Product extends AuditableEntity {

  @ManyToOne(optional = false)
  @JoinColumn(name = "category_id", nullable = false)
  private Category category;

  @Column(nullable = false, length = 50)
  private String name;

  @Column(nullable = false)
  private String description;

  @Column(name = "price", nullable = false, precision = 6, scale = 2)
  private BigDecimal price;
}
