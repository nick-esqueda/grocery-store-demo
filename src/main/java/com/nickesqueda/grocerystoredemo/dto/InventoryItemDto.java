package com.nickesqueda.grocerystoredemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InventoryItemDto {
  private Integer id;
  private ProductWithCategoryDto product;
  private Integer quantity;
  private Integer quantityOnHold;
}
