package com.ryan.ddd.domain.common.exception;

/**
 * Domain exception representing an invariant violation or unsupported domain operation.
 *
 * <p>Keep this free of transport concepts (HTTP) and infrastructure details.
 */
public class DomainException extends RuntimeException {

  public DomainException(String message) {
    super(message);
  }

  public DomainException(String message, Throwable cause) {
    super(message, cause);
  }
}
