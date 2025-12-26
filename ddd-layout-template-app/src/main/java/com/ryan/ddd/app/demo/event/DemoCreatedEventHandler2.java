package com.ryan.ddd.app.demo.event;

import com.ryan.ddd.app.common.event.EventHandle;
import com.ryan.ddd.domain.demo.event.DemoCreatedEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DemoCreatedEventHandler2 implements EventHandle<DemoCreatedEvent> {

  @Override
  public void handler(DemoCreatedEvent event) {
    log.info("DemoCreatedEventHandler2 event handled. id: {}", event.getDemoId().toString());
  }

  @Override
  public String getType() {
    return DemoCreatedEvent.EVENT_TYPE;
  }

  @Override
  public Class<DemoCreatedEvent> payloadClass() {
    return DemoCreatedEvent.class;
  }
}

