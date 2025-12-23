package com.ryan.ddd.demo.adapter.api.command.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateDemoRequest {

  @NotBlank(message = "Name must not be blank")
  private String name;
}
