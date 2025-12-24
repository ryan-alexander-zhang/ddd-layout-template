package com.ryan.ddd.app.demo.event;

import com.ryan.ddd.common.event.EventHandler;
import com.ryan.ddd.domain.demo.event.DemoCreatedEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DemoCreatedEventHandler2 implements EventHandler<DemoCreatedEvent> {

  @Override
  public void handler(DemoCreatedEvent event) {
    log.info("DemoCreatedEventHandler2 event handled. id: {}", event.getDemoId().toString());
  }

  @Override
  public String type() {
    return DemoCreatedEvent.TYPE;
  }

  @Override
  public Class<DemoCreatedEvent> payloadClass() {
    return DemoCreatedEvent.class;
  }
}
