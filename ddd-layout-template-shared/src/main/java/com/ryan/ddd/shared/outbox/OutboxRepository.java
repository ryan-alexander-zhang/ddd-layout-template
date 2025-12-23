package com.ryan.ddd.shared.outbox;

import com.ryan.ddd.shared.event.EventEnvelope;

public interface OutboxRepository {
  void append(EventEnvelope<?> envelope);
}
