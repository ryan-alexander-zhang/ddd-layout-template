package com.ryan.ddd.adapter.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Response {

  private boolean success;

  private String errCode;

  private String errMessage;

  public static Response buildSuccess() {
    Response response = new Response();
    response.setSuccess(true);
    return response;
  }

  public static Response buildFailure(String errCode, String errMessage) {
    Response response = new Response();
    response.setSuccess(false);
    response.setErrCode(errCode);
    response.setErrMessage(errMessage);
    return response;
  }
}
