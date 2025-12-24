package com.ryan.ddd.domain.common.inbox;

import java.time.Instant;
import java.util.Optional;

public interface InboxRepository {

  TryStartResult tryStart(String messageId, String consumer, String payloadHash, Instant now);

  void markDone(String messageId, String consumer, Instant processedAt);

  void markFailed(String messageId, String consumer, String lastError, Instant now);

  Optional<InboxRecord> find(String messageId, String consumer);

  enum TryStartResult {
    STARTED,        // 抢到处理权
    ALREADY_DONE,   // 已处理完成
    IN_PROGRESS     // 正在处理或失败待重试(策略可调)
  }

  record InboxRecord(String messageId, String consumer, InboxStatus status, int retryCount) {

  }
}
