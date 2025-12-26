package com.ryan.ddd.start.bootstrap.config;

import com.ryan.ddd.app.common.event.EventHandle;
import com.ryan.ddd.app.common.event.HandlerRegistry;
import com.ryan.ddd.app.common.event.InboxGuard;
import com.ryan.ddd.domain.common.inbox.InboxRepository;
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
  public HandlerRegistry handlerRegistry(List<EventHandle<?>> eventHandles) {
    return new HandlerRegistry(eventHandles);
  }

}

