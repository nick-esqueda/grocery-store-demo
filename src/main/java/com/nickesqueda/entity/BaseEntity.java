package com.nickesqueda.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@MappedSuperclass
public class BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
}
