package com.ryan.ddd.app.event;

public enum OutboxStatus {
  PENDING,
  PROCESSING,
  PUBLISHED,
  FAILED
}
