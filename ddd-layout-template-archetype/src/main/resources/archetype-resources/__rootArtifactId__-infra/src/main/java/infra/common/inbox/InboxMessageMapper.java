#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.infra.common.inbox;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.time.Instant;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface InboxMessageMapper extends BaseMapper<InboxMessagePO> {

  // MySQL: INSERT IGNORE. 插入成功返回 1，已存在返回 0
  @Insert("""
        INSERT IGNORE INTO inbox_message
          (message_id, consumer, status, payload_hash, first_seen_at, last_seen_at, retry_count)
        VALUES
          (${symbol_pound}{messageId}, ${symbol_pound}{consumer}, ${symbol_pound}{status}, ${symbol_pound}{payloadHash}, ${symbol_pound}{now}, ${symbol_pound}{now}, 0)
      """)
  int tryInsertProcessing(@Param("messageId") String messageId,
      @Param("consumer") String consumer,
      @Param("status") String status,
      @Param("payloadHash") String payloadHash,
      @Param("now") Instant now);

  @Select("""
        SELECT status, retry_count
        FROM inbox_message
        WHERE message_id = ${symbol_pound}{messageId} AND consumer = ${symbol_pound}{consumer}
      """)
  InboxStatusRow selectStatus(@Param("messageId") String messageId,
      @Param("consumer") String consumer);

  @Update("""
        UPDATE inbox_message
        SET status = 'DONE',
            processed_at = ${symbol_pound}{processedAt},
            last_error = NULL
        WHERE message_id = ${symbol_pound}{messageId} AND consumer = ${symbol_pound}{consumer}
      """)
  int markDone(@Param("messageId") String messageId,
      @Param("consumer") String consumer,
      @Param("processedAt") Instant processedAt);

  @Update("""
        UPDATE inbox_message
        SET status = 'FAILED',
            retry_count = retry_count + 1,
            last_error = ${symbol_pound}{lastError}
        WHERE message_id = ${symbol_pound}{messageId} AND consumer = ${symbol_pound}{consumer}
      """)
  int markFailed(@Param("messageId") String messageId,
      @Param("consumer") String consumer,
      @Param("lastError") String lastError);

  record InboxStatusRow(String status, int retryCount) {

  }
}
