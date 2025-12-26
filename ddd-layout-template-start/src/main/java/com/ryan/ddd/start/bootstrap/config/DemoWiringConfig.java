package com.ryan.ddd.start.bootstrap.config;

import com.ryan.ddd.app.common.TranslatingCommandHandler;
import com.ryan.ddd.app.demo.command.dto.CreateDemoCommand;
import com.ryan.ddd.app.demo.command.dto.CreateDemoResult;
import com.ryan.ddd.app.demo.command.handler.CreateDemoCommandHandler;
import com.ryan.ddd.app.demo.event.DemoCreatedEventHandler;
import com.ryan.ddd.app.demo.event.DemoCreatedEventHandler2;
import com.ryan.ddd.app.demo.query.dto.GetDemoDetailQuery;
import com.ryan.ddd.app.demo.query.handler.GetDemoDetailQueryHandler;
import com.ryan.ddd.app.demo.query.query.DemoQueries;
import com.ryan.ddd.app.demo.query.view.GetDemoDetailView;
import com.ryan.ddd.domain.common.outbox.OutboxRepository;
import com.ryan.ddd.domain.demo.repository.DemoRepository;
import java.util.Optional;
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

  /**
   * Application boundary: translate any domain/infra failures into AppException.
   */
  @Bean
  public TranslatingCommandHandler<CreateDemoCommand, CreateDemoResult> createDemoCommandHandler(
      DemoRepository demoRepository,
      OutboxRepository outboxRepository) {
    return new TranslatingCommandHandler<>(
        new CreateDemoCommandHandler(demoRepository, outboxRepository)
    );
  }

  /**
   * Application boundary: translate any domain/infra failures into AppException.
   */
  @Bean
  public TranslatingCommandHandler<GetDemoDetailQuery, Optional<GetDemoDetailView>> getDemoDetailQueryHandler(
      DemoQueries demoQueries) {
    return new TranslatingCommandHandler<>(new GetDemoDetailQueryHandler(demoQueries));
  }
}
