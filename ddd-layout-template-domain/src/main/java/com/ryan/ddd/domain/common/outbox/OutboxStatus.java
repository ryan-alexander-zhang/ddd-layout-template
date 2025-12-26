package com.ryan.ddd.domain.common.outbox;

public class OutboxStatus {

  private OutboxStatus() {
  }

  public static final String PENDING = "PENDING";
  public static final String SENT = "SENT";
  public static final String PROCESSING = "PROCESSING";
  public static final String DEAD = "DEAD";
}
