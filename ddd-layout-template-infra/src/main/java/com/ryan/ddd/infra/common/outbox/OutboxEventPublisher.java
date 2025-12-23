package com.ryan.ddd.infra.common.outbox;

import com.ryan.ddd.domain.common.event.EventEnvelope;

public interface OutboxEventPublisher {

  void publish(EventEnvelope<?> envelope) throws Exception;
}
