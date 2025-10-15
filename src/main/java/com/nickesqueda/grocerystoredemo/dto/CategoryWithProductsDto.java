package com.nickesqueda.grocerystoredemo.dto;

import java.util.List;
import lombok.Data;

@Data
public class CategoryWithProductsDto {
  private Integer id;
  private String name;
  private String description;
  private List<ProductDto> products;
}
