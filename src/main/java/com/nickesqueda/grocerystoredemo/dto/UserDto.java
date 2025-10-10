package com.nickesqueda.grocerystoredemo.dto;

import com.nickesqueda.grocerystoredemo.model.entity.RoleName;

import java.time.Instant;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
  private Integer id;
  private String username;
  private String firstName;
  private String lastName;
  private String address;
  private String email;
  private String phoneNumber;
  private String recentPaymentMethodToken;
  private Set<RoleName> roles;
  private Instant createdAt;
  private Instant updatedAt;
}
