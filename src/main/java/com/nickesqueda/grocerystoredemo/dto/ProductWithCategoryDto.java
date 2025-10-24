package com.nickesqueda.grocerystoredemo.dto;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductWithCategoryDto {
  private Integer id;
  private CategoryDto category;
  private String name;
  private String description;
  private BigDecimal price;
}
