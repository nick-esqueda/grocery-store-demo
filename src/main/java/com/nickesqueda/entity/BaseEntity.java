package com.nickesqueda.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@MappedSuperclass
public class BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
}
