package com.ryan.ddd.adapter.demo;

import jakarta.validation.constraints.NotBlank;

public class CreateOrderRequest {
  @NotBlank
  private String orderId;

  @NotBlank
  private String customerId;

  public String getOrderId() {
    return orderId;
  }

  public void setOrderId(String orderId) {
    this.orderId = orderId;
  }

  public String getCustomerId() {
    return customerId;
  }

  public void setCustomerId(String customerId) {
    this.customerId = customerId;
  }
}
