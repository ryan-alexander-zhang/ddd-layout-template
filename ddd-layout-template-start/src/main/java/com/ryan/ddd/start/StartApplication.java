package com.ryan.ddd.start;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.ryan.ddd")
public class StartApplication {
  public static void main(String[] args) {
    SpringApplication.run(StartApplication.class, args);
  }
}
