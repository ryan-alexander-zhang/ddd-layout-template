package com.ryan.ddd.infra.common.outbox;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("outbox_event")
public class OutboxEventPO {

  @TableId
  private UUID id;

  private String type;
  private String payload;
  private Instant occurredAt;

  /**
   * @see com.ryan.ddd.domain.common.outbox.OutboxStatus
   */
  private String status;
  private int attempts;
  private Instant nextAttemptAt;
  private String lastError;

  private String lockedBy;
  private Instant lockedAt;
}
