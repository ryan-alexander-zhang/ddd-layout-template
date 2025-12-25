#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.start.bootstrap.config;

import ${package}.common.event.EventHandler;
import ${package}.common.event.HandlerRegistry;
import ${package}.common.event.InboxGuard;
import ${package}.domain.common.inbox.InboxRepository;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CommonWiringConfig {

  @Bean
  public InboxGuard inboxGuard(InboxRepository inboxRepository) {
    return new InboxGuard(inboxRepository);
  }

  @Bean
  public HandlerRegistry handlerRegistry(List<EventHandler<?>> eventHandlers) {
    return new HandlerRegistry(eventHandlers);
  }

}
