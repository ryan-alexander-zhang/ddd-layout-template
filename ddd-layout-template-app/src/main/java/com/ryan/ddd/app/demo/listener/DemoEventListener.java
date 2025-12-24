package com.ryan.ddd.app.demo.listener;

import com.ryan.ddd.domain.common.event.EventEnvelope;
import com.ryan.ddd.domain.demo.event.DemoCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

// TODO for now only.
@Component
@Slf4j
public class DemoEventListener {

  @EventListener
  public void on(EventEnvelope<?> envelope) {
    if (!DemoCreatedEvent.TYPE.equals(envelope.getType())) {
      return;
    }
  }

  @EventListener
  public void onCreate(EventEnvelope<?> envelope) {
    if (!DemoCreatedEvent.TYPE.equals(envelope.getType())) {
      return;
    }
  }
}
