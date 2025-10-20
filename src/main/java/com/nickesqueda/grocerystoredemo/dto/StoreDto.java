package com.nickesqueda.grocerystoredemo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoreDto {
  private Integer id;
  private String address;
  private Integer totalPickupSpots;
}
