package com.ryan.ddd.app.event;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface EventOutboxRepository {
  void save(EventOutboxRecord record);

  Optional<EventOutboxRecord> findById(String id);

  List<EventOutboxRecord> fetchPending(Instant now, int batchSize);

  void update(EventOutboxRecord record);
}
