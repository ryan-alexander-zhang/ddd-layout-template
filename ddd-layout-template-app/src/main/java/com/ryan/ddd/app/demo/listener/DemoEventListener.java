package com.ryan.ddd.app.demo.listener;

import com.ryan.ddd.domain.common.event.EventEnvelope;
import com.ryan.ddd.domain.common.inbox.InboxRepository;
import com.ryan.ddd.domain.demo.event.DemoCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;

@Component
@Slf4j
public class DemoEventListener {

  private static final String CONSUMER = "spring-event:DemoEventListener:demo.created";

  private final InboxRepository inboxRepository;

  public DemoEventListener(InboxRepository inboxRepository) {
    this.inboxRepository = inboxRepository;
  }

  @EventListener
  @Transactional
  public void onDemoCreateEvent(EventEnvelope<?> envelope) {
    if (!DemoCreatedEvent.TYPE.equals(envelope.getType())) {
      return;
    }

    DemoCreatedEvent event = (DemoCreatedEvent) envelope.getPayload();

    String messageId = envelope.getId().toString();
    String payloadHash = sha256Hex(event.getDemoId().toString());
    Instant now = Instant.now();

    InboxRepository.TryStartResult r =
        inboxRepository.tryStart(messageId, CONSUMER, payloadHash, now);

    if (r == InboxRepository.TryStartResult.ALREADY_DONE) {
      log.info("Skip duplicated message. messageId={}, consumer={}", messageId, CONSUMER);
      return;
    }
    if (r == InboxRepository.TryStartResult.IN_PROGRESS) {
      log.info("Skip in-progress message. messageId={}, consumer={}", messageId, CONSUMER);
      return;
    }

    try {
      log.info("Handle demo.created. demoId={}, messageId={}", event.getDemoId(), messageId);

      // 模拟业务逻辑
      System.out.println("demoId=" + event.getDemoId());
      throw new RuntimeException("demoId=" + event.getDemoId());

//      inboxRepository.markDone(messageId, CONSUMER, Instant.now());
    } catch (Exception ex) {
      inboxRepository.markFailed(messageId, CONSUMER, safeErr(ex), Instant.now());
      throw ex;
    }
  }

  private static String sha256Hex(String s) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] digest = md.digest(s.getBytes(StandardCharsets.UTF_8));
      StringBuilder sb = new StringBuilder(digest.length * 2);
      for (byte b : digest) sb.append(String.format("%02x", b));
      return sb.toString();
    } catch (Exception e) {
      return null;
    }
  }

  private static String safeErr(Exception ex) {
    String m = ex.getMessage();
    if (m == null) return ex.getClass().getSimpleName();
    return m.length() > 1000 ? m.substring(0, 1000) : m;
  }
}
