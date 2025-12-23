package com.ryan.ddd.app.demo.query.view;

import java.util.UUID;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class GetDemoDetailView {
  private UUID id;
  private String name;
}
