package com.ryan.ddd.start.bootstrap.config;

import com.ryan.ddd.app.demo.command.handler.CreateDemoCommandHandler;
import com.ryan.ddd.app.demo.query.handler.GetDemoDetailQueryHandler;
import com.ryan.ddd.app.demo.query.query.DemoQueries;
import com.ryan.ddd.domain.demo.repository.DemoRepository;
import com.ryan.ddd.shared.outbox.OutboxRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppWiringConfig {

  @Bean
  public CreateDemoCommandHandler createDemoCommandHandler(DemoRepository demoRepository, OutboxRepository outboxRepository) {
    return new CreateDemoCommandHandler(demoRepository, outboxRepository);
  }

  @Bean
  public GetDemoDetailQueryHandler getDemoQueryHandler(DemoQueries demoQueries) {
    return new GetDemoDetailQueryHandler(demoQueries);
  }
}
