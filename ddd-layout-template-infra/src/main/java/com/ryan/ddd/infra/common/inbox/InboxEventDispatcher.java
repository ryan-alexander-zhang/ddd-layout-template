package com.ryan.ddd.infra.common.inbox;

import com.ryan.ddd.common.event.EventHandler;
import com.ryan.ddd.common.event.HandlerRegistry;
import com.ryan.ddd.common.event.InboxGuard;
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
  private final HandlerRegistry registry;

  public InboxEventDispatcher(InboxGuard inboxGuard, HandlerRegistry registry) {
    this.inboxGuard = inboxGuard;
    this.registry = registry;
  }

  @Transactional
  public void dispatch(EventEnvelope<?> envelope) {
    EventHandler<?> handler = registry.get(envelope.getType());
    if (handler == null) {
      return;
    }

    String messageId = envelope.getId().toString();
    String consumer = consumerKey(handler, envelope.getType());
    String payloadHash = sha256Hex(String.valueOf(envelope.getPayload()));

    InboxRepository.TryStartResult r = inboxGuard.tryStart(messageId, consumer, payloadHash);
    if (r != InboxRepository.TryStartResult.STARTED) {
      return;
    }

    try {
      invoke(handler, envelope.getPayload());
      inboxGuard.markDone(messageId, consumer);
    } catch (Exception ex) {
      inboxGuard.markFailed(messageId, consumer, safeErr(ex));
      throw ex;
    }
  }

  private static String consumerKey(EventHandler<?> h, String type) {
    return h.consumerId() + ":" + type;
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

  private static <T extends DomainEvent> void invoke(EventHandler<T> handler, Object payload) {
    T typedPayload = handler.payloadClass().cast(payload);
    handler.handler(typedPayload);
  }

  private static String safeErr(Exception ex) {
    String m = ex.getMessage();
    if (m == null) {
      return ex.getClass().getSimpleName();
    }
    return m.length() > 1000 ? m.substring(0, 1000) : m;
  }
}
