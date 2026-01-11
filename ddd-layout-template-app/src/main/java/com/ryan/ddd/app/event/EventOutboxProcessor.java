package com.ryan.ddd.app.event;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EventOutboxProcessor {
  private static final Logger logger = LoggerFactory.getLogger(EventOutboxProcessor.class);

  private final EventOutboxRepository repository;
  private final EventBus eventBus;
  private final EventDispatcher dispatcher;
  private final EventDedupStore dedupStore;
  private final Duration dedupTtl;
  private final Duration retryDelay;
  private final Clock clock;
  private final Executor executor;

  public EventOutboxProcessor(EventOutboxRepository repository, EventBus eventBus,
      EventDispatcher dispatcher, EventDedupStore dedupStore, Duration dedupTtl,
      Duration retryDelay, Clock clock, Executor executor) {
    this.repository = Objects.requireNonNull(repository, "repository");
    this.eventBus = Objects.requireNonNull(eventBus, "eventBus");
    this.dispatcher = Objects.requireNonNull(dispatcher, "dispatcher");
    this.dedupStore = Objects.requireNonNull(dedupStore, "dedupStore");
    this.dedupTtl = Objects.requireNonNull(dedupTtl, "dedupTtl");
    this.retryDelay = Objects.requireNonNull(retryDelay, "retryDelay");
    this.clock = Objects.requireNonNull(clock, "clock");
    this.executor = Objects.requireNonNull(executor, "executor");
  }

  public void processBatch(int batchSize) {
    Instant now = Instant.now(clock);
    List<EventOutboxRecord> records = repository.fetchPending(now, batchSize);
    for (EventOutboxRecord record : records) {
      processAsync(record);
    }
  }

  public void processSync(EventOutboxRecord record) {
    handleRecord(record);
  }

  public void processAsync(EventOutboxRecord record) {
    executor.execute(() -> handleRecord(record));
  }

  private void handleRecord(EventOutboxRecord record) {
    EventOutboxRecord inProgress = record.withStatus(OutboxStatus.PROCESSING,
        record.retryCount(), record.nextAttemptAt());
    repository.update(inProgress);
    boolean firstTime = dedupStore.markIfNotProcessed(record.envelope().eventId(), dedupTtl);
    if (!firstTime) {
      logger.info("Skip duplicate event {}", record.envelope().eventId());
      repository.update(record.withStatus(OutboxStatus.PUBLISHED, record.retryCount(),
          Instant.now(clock)));
      return;
    }

    try {
      eventBus.publish(record.envelope());
      dispatcher.dispatch(record.envelope());
      repository.update(record.withStatus(OutboxStatus.PUBLISHED, record.retryCount(),
          Instant.now(clock)));
    } catch (Exception ex) {
      int nextRetry = record.retryCount() + 1;
      Instant nextAttempt = Instant.now(clock).plus(retryDelay.multipliedBy(nextRetry));
      logger.warn("Failed to publish event {}, retrying", record.envelope().eventId(), ex);
      repository.update(record.withStatus(OutboxStatus.FAILED, nextRetry, nextAttempt));
    }
  }
}
