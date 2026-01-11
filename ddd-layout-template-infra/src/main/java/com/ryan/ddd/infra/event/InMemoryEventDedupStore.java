package com.ryan.ddd.infra.event;

import com.ryan.ddd.app.event.EventDedupStore;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryEventDedupStore implements EventDedupStore {
  private final Map<String, Instant> cache = new ConcurrentHashMap<>();

  @Override
  public boolean markIfNotProcessed(String eventId, Duration ttl) {
    Instant now = Instant.now();
    cache.entrySet().removeIf(entry -> entry.getValue().isBefore(now));
    return cache.putIfAbsent(eventId, now.plus(ttl)) == null;
  }
}
