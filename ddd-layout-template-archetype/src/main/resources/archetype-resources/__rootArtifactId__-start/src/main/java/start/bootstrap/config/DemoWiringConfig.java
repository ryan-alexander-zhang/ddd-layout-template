#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.start.bootstrap.config;

import ${package}.app.demo.command.handler.CreateDemoCommandHandler;
import ${package}.app.demo.event.DemoCreatedEventHandler;
import ${package}.app.demo.event.DemoCreatedEventHandler2;
import ${package}.app.demo.query.handler.GetDemoDetailQueryHandler;
import ${package}.app.demo.query.query.DemoQueries;
import ${package}.domain.common.outbox.OutboxRepository;
import ${package}.domain.demo.repository.DemoRepository;
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
