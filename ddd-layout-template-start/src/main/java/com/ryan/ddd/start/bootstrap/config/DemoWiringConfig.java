package com.ryan.ddd.start.bootstrap.config;

import com.ryan.ddd.app.demo.command.handler.CreateDemoCommandHandler;
import com.ryan.ddd.app.demo.event.DemoCreatedEventHandler;
import com.ryan.ddd.app.demo.event.DemoCreatedEventHandler2;
import com.ryan.ddd.app.demo.query.handler.GetDemoDetailQueryHandler;
import com.ryan.ddd.app.demo.query.query.DemoQueries;
import com.ryan.ddd.domain.common.outbox.OutboxRepository;
import com.ryan.ddd.domain.demo.repository.DemoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DemoWiringConfig {

  @Bean
  public DemoCreatedEventHandler demoCreatedEventHandler() {
    return new DemoCreatedEventHandler();
  }

  @Bean
  public DemoCreatedEventHandler2 demoCreatedEventHandler2() {
    return new DemoCreatedEventHandler2();
  }

  @Bean
  public CreateDemoCommandHandler createDemoCommandHandler(DemoRepository demoRepository,
      OutboxRepository outboxRepository) {
    return new CreateDemoCommandHandler(demoRepository, outboxRepository);
  }

  @Bean
  public GetDemoDetailQueryHandler getDemoDetailQueryHandler(DemoQueries demoQueries) {
    return new GetDemoDetailQueryHandler(demoQueries);
  }
}
