package com.ryan.ddd.domain.common.outbox;


import com.ryan.ddd.domain.common.event.EventEnvelope;

public interface OutboxRepository {
  void append(EventEnvelope<?> envelope);
}
