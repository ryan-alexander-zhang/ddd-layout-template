package com.ryan.ddd.adapter.demo.adapter.api.query.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GetDemoDetailResponse {
  private final String id;
  private final String name;
}
