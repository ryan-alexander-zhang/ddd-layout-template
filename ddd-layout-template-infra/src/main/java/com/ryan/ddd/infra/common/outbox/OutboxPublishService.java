package com.ryan.ddd.infra.common.outbox;

import com.ryan.ddd.shared.outbox.OutboxStatus;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OutboxPublishService {

  private static final int BATCH_SIZE = 50;
  private static final int MAX_ATTEMPTS = 10;
  private static final Duration LOCK_TTL = Duration.ofSeconds(30);

  private final OutboxEventMapper mapper;
  private final OutboxEventSerde serde;
  private final OutboxEventPublisher publisher;

  public OutboxPublishService(OutboxEventMapper mapper,
      OutboxEventSerde serde,
      OutboxEventPublisher publisher) {
    this.mapper = mapper;
    this.serde = serde;
    this.publisher = publisher;
  }

  @Transactional
  public List<OutboxEventPO> claim(String workerId) {
    Instant now = Instant.now();
    Instant lockExpireAt = now.minus(LOCK_TTL);

    int n = mapper.claimBatch(
        OutboxStatus.PENDING,
        OutboxStatus.PROCESSING,
        workerId,
        now,
        lockExpireAt,
        BATCH_SIZE
    );
    if (n == 0) {
      return List.of();
    }
    return mapper.selectClaimed(OutboxStatus.PROCESSING, workerId, now);
  }

  public void publish(List<OutboxEventPO> batch) {
    for (OutboxEventPO po : batch) {
      try {
        publisher.publish(serde.toEnvelope(po));
        mapper.markSent(po.getId(), OutboxStatus.SENT);
      } catch (Exception ex) {
        int attempts = po.getAttempts() + 1;
        boolean dead = attempts >= MAX_ATTEMPTS;

        long backoffSec = Math.min(300, (long) Math.pow(2, Math.min(attempts, 8)));
        mapper.markRetryOrDead(
            po.getId(),
            dead ? OutboxStatus.DEAD : OutboxStatus.PENDING,
            attempts,
            Instant.now().plusSeconds(backoffSec),
            safeMsg(ex)
        );
      }
    }
  }

  private static String safeMsg(Exception ex) {
    String m = ex.getMessage();
    if (m == null) {
      return ex.getClass().getSimpleName();
    }
    return m.length() > 500 ? m.substring(0, 500) : m;
  }
}
