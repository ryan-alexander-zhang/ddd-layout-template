#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.infra.common.outbox;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import org.apache.ibatis.annotations.*;

@Mapper
public interface OutboxEventMapper extends BaseMapper<OutboxEventPO> {

  @Update("""
    UPDATE outbox_event
    SET status = ${symbol_pound}{processing},
        locked_by = ${symbol_pound}{lockedBy},
        locked_at = ${symbol_pound}{now}
    WHERE status = ${symbol_pound}{pending}
      AND next_attempt_at <= ${symbol_pound}{now}
      AND (locked_by IS NULL OR locked_at < ${symbol_pound}{lockExpireAt})
    ORDER BY occurred_at
    LIMIT ${symbol_pound}{batchSize}
  """)
  int claimBatch(@Param("pending") String pending,
      @Param("processing") String processing,
      @Param("lockedBy") String lockedBy,
      @Param("now") Instant now,
      @Param("lockExpireAt") Instant lockExpireAt,
      @Param("batchSize") int batchSize);

  @Select("""
    SELECT * FROM outbox_event
    WHERE status = ${symbol_pound}{processing}
      AND locked_by = ${symbol_pound}{lockedBy}
      AND locked_at = ${symbol_pound}{lockedAt}
    ORDER BY occurred_at
  """)
  List<OutboxEventPO> selectClaimed(@Param("processing") String processing,
      @Param("lockedBy") String lockedBy,
      @Param("lockedAt") Instant lockedAt);

  @Update("""
    UPDATE outbox_event
    SET status = ${symbol_pound}{sent}, last_error = NULL, locked_by = NULL, locked_at = NULL
    WHERE id = ${symbol_pound}{id}
  """)
  int markSent(@Param("id") UUID id, @Param("sent") String sent);

  @Update("""
    UPDATE outbox_event
    SET status = ${symbol_pound}{status},
        attempts = ${symbol_pound}{attempts},
        next_attempt_at = ${symbol_pound}{nextAttemptAt},
        last_error = ${symbol_pound}{lastError},
        locked_by = NULL,
        locked_at = NULL
    WHERE id = ${symbol_pound}{id}
  """)
  int markRetryOrDead(@Param("id") UUID id,
      @Param("status") String status,
      @Param("attempts") int attempts,
      @Param("nextAttemptAt") Instant nextAttemptAt,
      @Param("lastError") String lastError);
}
