package com.ryan.ddd.infra.event;

import com.ryan.ddd.app.event.EventBus;
import com.ryan.ddd.app.event.EventEnvelope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingEventBus implements EventBus {
  private static final Logger logger = LoggerFactory.getLogger(LoggingEventBus.class);

  @Override
  public void publish(EventEnvelope envelope) {
    logger.info("Publish event: {}", envelope.eventType());
  }
}
