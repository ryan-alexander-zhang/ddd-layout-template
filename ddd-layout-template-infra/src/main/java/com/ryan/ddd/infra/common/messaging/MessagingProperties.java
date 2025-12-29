package com.ryan.ddd.infra.common.messaging;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@ConfigurationProperties(prefix = "messaging")
public class MessagingProperties {

  private final Kafka kafka = new Kafka();
  /**
   * Messaging mode.
   *
   * <p>SPRING: publish in-process using Spring ApplicationEventPublisher and consume via
   * EventListener </p>
   * <p>KAFKA: publish to Kafka and consume via @KafkaListener
   */
  @Setter
  private Mode mode = Mode.SPRING;

  public enum Mode {
    SPRING,
    KAFKA
  }

  @Setter
  @Getter
  public static class Kafka {

    private String topic = "ddd-events";
    private String consumerGroupId = "ddd-layout-template";

  }

}
