package com.ryan.ddd.app.event;

import java.time.Duration;

public interface EventDedupStore {
  boolean markIfNotProcessed(String eventId, Duration ttl);
}
