package com.ryan.ddd.infra.common.outbox;

import com.ryan.ddd.shared.event.EventEnvelope;

public interface OutboxEventPublisher {
  void publish(EventEnvelope<?> envelope) throws Exception;
}
