package com.ryan.ddd.demo.app.command.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.ryan.ddd.app.demo.command.dto.CreateDemoCommand;
import org.junit.jupiter.api.Test;

class CreateDemoCommandTest {

  @Test
  void builder() {
    CreateDemoCommand buildCommand = CreateDemoCommand.builder().name("test-name").build();
    assertEquals("test-name", buildCommand.getName());
  }
}