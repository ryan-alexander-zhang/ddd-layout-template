package com.ryan.ddd.infra.common.outbox;

import java.util.List;
import java.util.UUID;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class OutboxPublisherJob {

  private final OutboxPublishService service;
  private final String workerId = UUID.randomUUID().toString();

  public OutboxPublisherJob(OutboxPublishService service) {
    this.service = service;
  }

  @Scheduled(fixedDelayString = "${outbox.publisher.fixedDelayMs:2000}")
  public void publish() {
    List<OutboxEventPO> batch = service.claim(workerId);
    if (batch.isEmpty()) {
      return;
    }
    service.publish(batch);
  }
}
