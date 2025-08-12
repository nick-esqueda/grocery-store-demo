package com.nickesqueda.model.pickup;

public enum PickupStatus {
  PENDING,      // pickup window not started
  IN_PROGRESS,  // pickup window has started
  ARRIVED,      // user arrived at store, awaiting order
  COMPLETED,    // user picked up order successfully
  CANCELLED,    // user explicitly cancelled pickup
  NO_SHOW,      // user did not arrive within pickup window
}
