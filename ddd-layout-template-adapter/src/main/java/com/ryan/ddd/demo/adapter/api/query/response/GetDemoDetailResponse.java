package com.ryan.ddd.demo.adapter.api.query.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class GetDemoDetailResponse {
  private final String id;
  private final String name;
}
