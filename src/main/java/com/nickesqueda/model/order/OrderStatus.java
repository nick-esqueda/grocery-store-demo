package com.nickesqueda.model.order;

public enum OrderStatus {
  // Order was placed and all related actions completed successfully
  PLACED,

  // User has completed pickup, finalizing the order
  COMPLETED,

  // User explicitly cancelled the order after being placed and before pickup
  USER_CANCELLED,

  // Order cancelled due to pickup no-show
  NO_SHOW_CANCELLED,

  // Order cancelled due to a technical issue
  SYSTEM_CANCELLED,
}
