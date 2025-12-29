package com.ryan.ddd.infra.common.inbox;

import com.ryan.ddd.app.common.event.EventHandle;
import com.ryan.ddd.app.common.event.HandlerRegistry;
import com.ryan.ddd.app.common.event.InboxGuard;
import com.ryan.ddd.domain.common.event.DomainEvent;
import com.ryan.ddd.domain.common.event.EventEnvelope;
import com.ryan.ddd.domain.common.inbox.InboxRepository;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class InboxEventDispatcher {

  private final InboxGuard inboxGuard;
  private final HandlerRegistry handlerRegistry;

  public InboxEventDispatcher(InboxGuard inboxGuard, HandlerRegistry handlerRegistry) {
    this.inboxGuard = inboxGuard;
    this.handlerRegistry = handlerRegistry;
  }

  @Transactional
  public void dispatch(EventEnvelope<?> envelope) {
    handlerRegistry.forEachHandler(envelope.getType(), h -> dispatchToOne(h, envelope));
  }

  private void dispatchToOne(EventHandle<? extends DomainEvent> h, EventEnvelope<?> envelope) {
    String messageId = envelope.getEventId().toString();

    // 关键：同一条消息，不同处理器必须不同 consumer，避免互相抢幂等
    String consumer = consumerKey(h, envelope.getType());

    String payloadHash = sha256Hex(String.valueOf(envelope.getPayload()));

    InboxRepository.TryStartResult r = inboxGuard.tryStart(messageId, consumer, payloadHash);
    if (r != InboxRepository.TryStartResult.STARTED) {
      return;
    }

    try {
      invokeTyped(h, envelope.getPayload());
      inboxGuard.markDone(messageId, consumer);
    } catch (Exception ex) {
      inboxGuard.markFailed(messageId, consumer, safeErr(ex));
      throw ex;
    }
  }

  private static String consumerKey(EventHandle<?> h, String type) {
    return "spring-event:" + h.consumerId() + ":" + type;
  }

  private static String sha256Hex(String s) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] digest = md.digest(s.getBytes(StandardCharsets.UTF_8));
      StringBuilder sb = new StringBuilder(digest.length * 2);
      for (byte b : digest) {
        sb.append(String.format("%02x", b));
      }
      return sb.toString();
    } catch (Exception e) {
      return null;
    }
  }

  private static <T extends DomainEvent> void invokeTyped(EventHandle<T> h, Object payload) {
    Class<T> clazz = h.payloadClass();
    if (!clazz.isInstance(payload)) {
      throw new IllegalArgumentException(
          "Payload type mismatch. handler=" + h.consumerId()
              + ", expected=" + clazz.getName()
              + ", actual=" + (payload == null ? "null" : payload.getClass().getName()));
    }
    h.handler(clazz.cast(payload));
  }

  private static String safeErr(Exception ex) {
    String m = ex.getMessage();
    if (m == null) {
      return ex.getClass().getSimpleName();
    }
    return m.length() > 1000 ? m.substring(0, 1000) : m;
  }
}
