package com.ryan.ddd.infra.common.inbox;

import com.ryan.ddd.domain.common.inbox.InboxRepository;
import com.ryan.ddd.domain.common.inbox.InboxStatus;
import java.time.Instant;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class InboxRepositoryImpl implements InboxRepository {

  private final InboxMessageMapper mapper;

  public InboxRepositoryImpl(InboxMessageMapper mapper) {
    this.mapper = mapper;
  }

  @Override
  public TryStartResult tryStart(String messageId, String consumer, String payloadHash, Instant now) {
    int inserted = mapper.tryInsertProcessing(messageId, consumer, InboxStatus.PROCESSING.name(), payloadHash, now);
    if (inserted == 1) {
      return TryStartResult.STARTED;
    }

    var row = mapper.selectStatus(messageId, consumer);
    if (row == null) {
      // 极端情况：并发下刚插入又回滚/不可见，按 IN_PROGRESS 处理
      return TryStartResult.IN_PROGRESS;
    }

    InboxStatus st = InboxStatus.valueOf(row.status());
    if (st == InboxStatus.DONE) {
      return TryStartResult.ALREADY_DONE;
    }
    return TryStartResult.IN_PROGRESS; // PROCESSING 或 FAILED 的策略：默认不重复执行业务
  }

  @Override
  public void markDone(String messageId, String consumer, Instant processedAt) {
    mapper.markDone(messageId, consumer, processedAt);
  }

  @Override
  public void markFailed(String messageId, String consumer, String lastError, Instant now) {
    mapper.markFailed(messageId, consumer, safeMsg(lastError));
  }

  @Override
  public Optional<InboxRecord> find(String messageId, String consumer) {
    var row = mapper.selectStatus(messageId, consumer);
    if (row == null) return Optional.empty();
    return Optional.of(new InboxRecord(messageId, consumer, InboxStatus.valueOf(row.status()), row.retryCount()));
  }

  private static String safeMsg(String m) {
    if (m == null) return "error";
    return m.length() > 1000 ? m.substring(0, 1000) : m;
  }
}
