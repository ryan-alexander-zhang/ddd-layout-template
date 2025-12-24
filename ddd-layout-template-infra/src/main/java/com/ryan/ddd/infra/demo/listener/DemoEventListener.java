//package com.ryan.ddd.infra.demo.listener;
//
//
//import com.ryan.ddd.app.demo.event.DemoCreatedEventHandler;
//import com.ryan.ddd.common.event.InboxGuard;
//import com.ryan.ddd.domain.common.event.EventEnvelope;
//import com.ryan.ddd.domain.common.inbox.InboxRepository;
//import com.ryan.ddd.domain.demo.event.DemoCreatedEvent;
//import java.nio.charset.StandardCharsets;
//import java.security.MessageDigest;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.event.EventListener;
//import org.springframework.stereotype.Component;
//import org.springframework.transaction.annotation.Transactional;
//
//@Component
//@Slf4j
//public class DemoEventListener {
//
//  // 语义 consumerId，固定不变，定义幂等边界
//  private static final String CONSUMER = "spring-event:DemoCreatedEventHandler:demo.created";
//
//  private final InboxGuard inboxGuard;
//  private final DemoCreatedEventHandler handler;
//
//  public DemoEventListener(InboxGuard inboxGuard, DemoCreatedEventHandler handler) {
//    this.inboxGuard = inboxGuard;
//    this.handler = handler;
//  }
//
//  @EventListener
//  @Transactional // 业务事务。业务失败要回滚业务写入（这里 demo 只有打印）
//  public void onDemoCreateEvent(EventEnvelope<?> envelope) {
//    if (!DemoCreatedEvent.TYPE.equals(envelope.getType())) {
//      return;
//    }
//
//    DemoCreatedEvent event = (DemoCreatedEvent) envelope.getPayload();
//
//    String messageId = envelope.getId().toString();
//    String payloadHash = sha256Hex(event.getDemoId().toString());
//
//    InboxRepository.TryStartResult r = inboxGuard.tryStart(messageId, CONSUMER, payloadHash);
//
//    if (r == InboxRepository.TryStartResult.ALREADY_DONE) {
//      log.debug("Skip duplicated. messageId={}, consumer={}", messageId, CONSUMER);
//      return;
//    }
//    if (r == InboxRepository.TryStartResult.IN_PROGRESS) {
//      log.debug("Skip in-progress. messageId={}, consumer={}", messageId, CONSUMER);
//      return;
//    }
//
//    try {
//      handler.handler(event);
//      inboxGuard.markDone(messageId, CONSUMER);
//    } catch (Exception ex) {
//      inboxGuard.markFailed(messageId, CONSUMER, safeErr(ex));
//      throw ex;
//    }
//  }
//
//  private static String sha256Hex(String s) {
//    try {
//      MessageDigest md = MessageDigest.getInstance("SHA-256");
//      byte[] digest = md.digest(s.getBytes(StandardCharsets.UTF_8));
//      StringBuilder sb = new StringBuilder(digest.length * 2);
//      for (byte b : digest) sb.append(String.format("%02x", b));
//      return sb.toString();
//    } catch (Exception e) {
//      return null;
//    }
//  }
//
//  private static String safeErr(Exception ex) {
//    String m = ex.getMessage();
//    if (m == null) return ex.getClass().getSimpleName();
//    return m.length() > 1000 ? m.substring(0, 1000) : m;
//  }
//}
