#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.infra.common.outbox;

import java.util.List;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class OutboxPublisherJob {

  private final OutboxPublishService service;
  private final String workerId = UUID.randomUUID().toString();

  public OutboxPublisherJob(OutboxPublishService service) {
    this.service = service;
  }

  @Scheduled(fixedDelayString = "${symbol_dollar}{outbox.publisher.fixedDelayMs:2000}")
  public void publish() {
    List<OutboxEventPO> batch = service.claim(workerId);
    if (batch.isEmpty()) {
      return;
    }
    service.publish(batch);
  }
}
