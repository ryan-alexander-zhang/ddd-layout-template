package com.ryan.ddd.infra.common.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ryan.ddd.infra.common.event.SpringDomainEventListener;
import com.ryan.ddd.infra.common.messaging.kafka.KafkaDomainEventListener;
import com.ryan.ddd.infra.common.messaging.kafka.KafkaOutboxEventPublisher;
import com.ryan.ddd.infra.common.inbox.InboxEventDispatcher;
import com.ryan.ddd.infra.common.outbox.OutboxEventPublisher;
import com.ryan.ddd.infra.common.outbox.OutboxEventTypeRegistry;
import com.ryan.ddd.infra.common.outbox.SpringOutboxEventPublisher;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;

@Configuration
@EnableConfigurationProperties(MessagingProperties.class)
public class MessagingInfraConfig {

  @Bean
  @ConditionalOnProperty(prefix = "messaging", name = "mode", havingValue = "SPRING", matchIfMissing = true)
  public OutboxEventPublisher springOutboxEventPublisher(ApplicationEventPublisher publisher) {
    return new SpringOutboxEventPublisher(publisher);
  }

  @Bean
  @ConditionalOnProperty(prefix = "messaging", name = "mode", havingValue = "KAFKA")
  public OutboxEventPublisher kafkaOutboxEventPublisher(
      KafkaTemplate<String, String> kafkaTemplate,
      ObjectMapper objectMapper,
      MessagingProperties props) {
    return new KafkaOutboxEventPublisher(kafkaTemplate, objectMapper, props.getKafka().getTopic(), props.getKafka().getConsumerGroupId());
  }

  @Bean
  @ConditionalOnProperty(prefix = "messaging", name = "mode", havingValue = "SPRING", matchIfMissing = true)
  public SpringDomainEventListener springDomainEventListener(InboxEventDispatcher dispatcher) {
    return new SpringDomainEventListener(dispatcher);
  }

  @Bean
  @ConditionalOnProperty(prefix = "messaging", name = "mode", havingValue = "KAFKA")
  public KafkaDomainEventListener kafkaDomainEventListener(
      InboxEventDispatcher dispatcher,
      OutboxEventTypeRegistry registry,
      ObjectMapper objectMapper) {
    return new KafkaDomainEventListener(dispatcher, registry, objectMapper);
  }
}

