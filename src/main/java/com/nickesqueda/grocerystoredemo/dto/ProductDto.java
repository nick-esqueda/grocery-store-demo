package com.nickesqueda.grocerystoredemo.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ProductDto {
  private Integer id;
  private String name;
  private String description;
  private BigDecimal price;
  private CategoryDto category;
}
