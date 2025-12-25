#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.app.demo.command.handler;

import ${package}.app.demo.command.dto.CreateDemoCommand;
import ${package}.app.demo.command.dto.CreateDemoResult;
import ${package}.common.CommandHandler;
import ${package}.domain.common.event.EventEnvelope;
import ${package}.domain.common.outbox.OutboxRepository;
import ${package}.domain.demo.event.DemoCreatedEvent;
import ${package}.domain.demo.model.Demo;
import ${package}.domain.demo.model.DemoName;
import ${package}.domain.demo.repository.DemoRepository;
import java.time.Instant;
import java.util.UUID;
import org.springframework.transaction.annotation.Transactional;

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
