package com.ryan.ddd.infra.common.inbox;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("inbox_message")
public class InboxMessagePO {

  @TableId
  private Long id;

  private String messageId;
  private String consumer;
  private String status;

  private String payloadHash;

  private Instant firstSeenAt;
  private Instant lastSeenAt;
  private Instant processedAt;

  private int retryCount;
  private String lastError;
}
