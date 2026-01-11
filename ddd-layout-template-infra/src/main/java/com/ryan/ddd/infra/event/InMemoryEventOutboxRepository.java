package com.ryan.ddd.infra.event;

import com.ryan.ddd.app.event.EventOutboxRecord;
import com.ryan.ddd.app.event.EventOutboxRepository;
import com.ryan.ddd.app.event.OutboxStatus;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryEventOutboxRepository implements EventOutboxRepository {
  private final ConcurrentHashMap<String, EventOutboxRecord> store = new ConcurrentHashMap<>();

  @Override
  public void save(EventOutboxRecord record) {
    store.put(record.id(), record);
  }

  @Override
  public Optional<EventOutboxRecord> findById(String id) {
    return Optional.ofNullable(store.get(id));
  }

  @Override
  public List<EventOutboxRecord> fetchPending(Instant now, int batchSize) {
    List<EventOutboxRecord> result = new ArrayList<>();
    for (EventOutboxRecord record : store.values()) {
      if (record.status() == OutboxStatus.PENDING || record.status() == OutboxStatus.FAILED) {
        if (!record.nextAttemptAt().isAfter(now)) {
          result.add(record);
        }
      }
      if (result.size() >= batchSize) {
        break;
      }
    }
    return result;
  }

  @Override
  public void update(EventOutboxRecord record) {
    store.put(record.id(), record);
  }
}
