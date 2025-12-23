package com.ryan.ddd.demo.adapter.api.command.request;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateDemoRequest {

  @NotBlank(message = "Name must not be blank")
  private String name;
}
