package com.ryan.ddd.app.workflow;

import java.time.Duration;

public final class RetryPolicy {
  private final int maxRetries;
  private final Duration backoff;

  public RetryPolicy(int maxRetries, Duration backoff) {
    if (maxRetries < 0) {
      throw new IllegalArgumentException("maxRetries cannot be negative");
    }
    this.maxRetries = maxRetries;
    this.backoff = backoff;
  }

  public int maxRetries() {
    return maxRetries;
  }

  public Duration backoff() {
    return backoff;
  }
}
