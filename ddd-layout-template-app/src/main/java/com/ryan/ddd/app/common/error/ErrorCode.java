package com.ryan.ddd.app.common.error;

import java.io.Serializable;

public interface ErrorCode extends Serializable {

  String code();

  int httpStatus();

  boolean retryable();

  String message();
}