package com.ryan.ddd.app.demo.command.handler;

import com.ryan.ddd.app.demo.command.dto.CreateDemoCommand;
import com.ryan.ddd.app.demo.command.dto.CreateDemoResult;
import com.ryan.ddd.base.CommandHandler;
import com.ryan.ddd.domain.demo.model.Demo;
import com.ryan.ddd.domain.demo.model.DemoName;
import com.ryan.ddd.domain.demo.repository.DemoRepository;

public class CreateDemoCommandHandler implements
    CommandHandler<CreateDemoCommand, CreateDemoResult> {

  private final DemoRepository demoRepository;

  public CreateDemoCommandHandler(DemoRepository demoRepository) {
    this.demoRepository = demoRepository;
  }

  @Override
  public CreateDemoResult handle(CreateDemoCommand command) {
    DemoName demoName = DemoName.of(command.getName());
    Demo demo = Demo.create(demoName);
    demoRepository.save(demo);
    return CreateDemoResult.builder().id(demo.getId().getValue()).build();
  }
}
