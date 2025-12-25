#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.common.event;

import ${package}.domain.common.inbox.InboxRepository;
import java.time.Instant;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class InboxGuard {

  private final InboxRepository inboxRepository;

  public InboxGuard(InboxRepository inboxRepository) {
    this.inboxRepository = inboxRepository;
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public InboxRepository.TryStartResult tryStart(String messageId, String consumer,
      String payloadHash) {
    return inboxRepository.tryStart(messageId, consumer, payloadHash, Instant.now());
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void markDone(String messageId, String consumer) {
    inboxRepository.markDone(messageId, consumer, Instant.now());
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void markFailed(String messageId, String consumer, String lastError) {
    inboxRepository.markFailed(messageId, consumer, lastError, Instant.now());
  }
}
