package com.ryan.ddd.app.demo.command.handler;

import com.ryan.ddd.app.demo.command.dto.CreateDemoCommand;
import com.ryan.ddd.app.demo.command.dto.CreateDemoResult;
import com.ryan.ddd.base.CommandHandler;
import com.ryan.ddd.domain.common.event.EventEnvelope;
import com.ryan.ddd.domain.common.outbox.OutboxRepository;
import com.ryan.ddd.domain.demo.event.DemoCreatedEvent;
import com.ryan.ddd.domain.demo.model.Demo;
import com.ryan.ddd.domain.demo.model.DemoName;
import com.ryan.ddd.domain.demo.repository.DemoRepository;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
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
        event.type(),
        event,
        Instant.now()
    ));
    return CreateDemoResult.builder().id(demo.getId().getValue()).build();
  }
}
