package com.ryan.ddd.app.common.exception;

import com.ryan.ddd.app.common.error.ErrorCode;
import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.Map;
import lombok.Getter;

@Getter
public class AppException extends RuntimeException {
  @Serial
  private static final long serialVersionUID = 1L;

  private final ErrorCode errorCode;
  private final Map<String, Serializable> attributes;

  public AppException(ErrorCode errorCode) {
    this(errorCode, errorCode.message(), null, Collections.emptyMap());
  }

  public AppException(ErrorCode errorCode, String message, Throwable cause,
      Map<String, Serializable> attributes) {
    super(message, cause);
    this.errorCode = errorCode;
    this.attributes = (attributes == null ? Collections.emptyMap() : Map.copyOf(attributes));
  }

  public AppException(ErrorCode errorCode, String message) {
    this(errorCode, message, null, Collections.emptyMap());
  }

  public AppException(ErrorCode errorCode, Throwable cause) {
    this(errorCode, errorCode.message(), cause, Collections.emptyMap());
  }
}
