package com.ryan.ddd.infra.common.outbox;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OutboxEventMapper extends BaseMapper<OutboxEventPO> {

  @Update("""
    UPDATE outbox_event
    SET status = #{processing},
        locked_by = #{lockedBy},
        locked_at = #{now}
    WHERE status = #{pending}
      AND next_attempt_at <= #{now}
      AND (locked_by IS NULL OR locked_at < #{lockExpireAt})
    ORDER BY occurred_at
    LIMIT #{batchSize}
  """)
  int claimBatch(@Param("pending") String pending,
      @Param("processing") String processing,
      @Param("lockedBy") String lockedBy,
      @Param("now") Instant now,
      @Param("lockExpireAt") Instant lockExpireAt,
      @Param("batchSize") int batchSize);

  @Select("""
    SELECT * FROM outbox_event
    WHERE status = #{processing}
      AND locked_by = #{lockedBy}
      AND locked_at = #{lockedAt}
    ORDER BY occurred_at
  """)
  List<OutboxEventPO> selectClaimed(@Param("processing") String processing,
      @Param("lockedBy") String lockedBy,
      @Param("lockedAt") Instant lockedAt);

  @Update("""
    UPDATE outbox_event
    SET status = #{sent}, last_error = NULL, locked_by = NULL, locked_at = NULL
    WHERE id = #{id}
  """)
  int markSent(@Param("id") UUID id, @Param("sent") String sent);

  @Update("""
    UPDATE outbox_event
    SET status = #{status},
        attempts = #{attempts},
        next_attempt_at = #{nextAttemptAt},
        last_error = #{lastError},
        locked_by = NULL,
        locked_at = NULL
    WHERE id = #{id}
  """)
  int markRetryOrDead(@Param("id") UUID id,
      @Param("status") String status,
      @Param("attempts") int attempts,
      @Param("nextAttemptAt") Instant nextAttemptAt,
      @Param("lastError") String lastError);
}
