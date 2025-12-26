package com.ryan.ddd.app.demo.command.handler;

import com.ryan.ddd.app.demo.command.dto.CreateDemoCommand;
import com.ryan.ddd.app.demo.command.dto.CreateDemoResult;
import com.ryan.ddd.app.common.CommandHandler;
import com.ryan.ddd.domain.common.event.EventEnvelope;
import com.ryan.ddd.domain.common.outbox.OutboxRepository;
import com.ryan.ddd.domain.demo.event.DemoCreatedEvent;
import com.ryan.ddd.domain.demo.model.Demo;
import com.ryan.ddd.domain.demo.model.DemoName;
import com.ryan.ddd.domain.demo.repository.DemoRepository;
import java.time.Instant;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public class CreateDemoCommandHandler implements
    CommandHandler<CreateDemoCommand, CreateDemoResult> {

  private final DemoRepository demoRepository;
  private final OutboxRepository outboxRepository;

  public CreateDemoCommandHandler(DemoRepository demoRepository,
      OutboxRepository outboxRepository) {
    this.demoRepository = demoRepository;
    this.outboxRepository = outboxRepository;
  }

  @Override
  @Transactional
  public CreateDemoResult handle(CreateDemoCommand command) {
    DemoName demoName = DemoName.of(command.getName());
    Demo demo = Demo.create(demoName);
    demoRepository.save(demo);

    DemoCreatedEvent event = new DemoCreatedEvent(demo.getId().getValue());
    outboxRepository.append(new EventEnvelope<>(
        UUID.randomUUID(),
        DemoCreatedEvent.EVENT_TYPE,
        event,
        Instant.now()
    ));
    log.info("demo created: {}", demo.getId().getValue());
    return CreateDemoResult.builder().id(demo.getId().getValue()).build();
  }
}
