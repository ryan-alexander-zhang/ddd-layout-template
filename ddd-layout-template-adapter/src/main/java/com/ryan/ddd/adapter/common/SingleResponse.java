package com.ryan.ddd.adapter.common;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SingleResponse<T> extends Response {

  private T data;

  public static SingleResponse<Void> buildSuccess() {
    SingleResponse<Void> response = new SingleResponse<>();
    response.setSuccess(true);
    return response;
  }

  public static SingleResponse<Void> buildFailure(String errCode, String errMessage) {
    SingleResponse<Void> response = new SingleResponse<>();
    response.setSuccess(false);
    response.setErrCode(errCode);
    response.setErrMessage(errMessage);
    return response;
  }

  public static <T> SingleResponse<T> of(T data) {
    SingleResponse<T> response = new SingleResponse<>();
    response.setSuccess(true);
    response.setData(data);
    return response;
  }
}
