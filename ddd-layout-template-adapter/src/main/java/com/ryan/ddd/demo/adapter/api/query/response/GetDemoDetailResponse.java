package com.ryan.ddd.demo.adapter.api.query.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Builder
public class GetDemoDetailResponse {
  private final String id;
  private final String name;
}
