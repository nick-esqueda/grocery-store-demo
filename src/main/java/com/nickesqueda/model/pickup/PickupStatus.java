package com.nickesqueda.model.pickup;

public enum PickupStatus {
  // Pickup scheduled, but window not started
  PENDING,

  // Pickup window has started
  IN_PROGRESS,

  // User arrived at store, awaiting order
  ARRIVED,

  // User picked up order successfully
  COMPLETED,

  // User explicitly cancelled pickup
  USER_CANCELLED,

  // User did not arrive within pickup window
  NO_SHOW,

  // Pickup cancelled due to a technical issue
  SYSTEM_CANCELLED,
}
