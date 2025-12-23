package com.ryan.ddd.start.bootstrap.config;

import com.ryan.ddd.app.demo.command.handler.CreateDemoCommandHandler;
import com.ryan.ddd.domain.demo.repository.DemoRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppWiringConfig {

  @Bean
  public CreateDemoCommandHandler createDemoCommandHandler(DemoRepository demoRepository) {
    return new CreateDemoCommandHandler(demoRepository);
  }
}
