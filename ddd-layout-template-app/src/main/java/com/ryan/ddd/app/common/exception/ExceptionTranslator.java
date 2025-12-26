package com.ryan.ddd.app.common.exception;

import com.ryan.ddd.app.common.error.CommonErrorCodes;
import com.ryan.ddd.app.common.error.ErrorCode;
import com.ryan.ddd.domain.common.exception.DomainException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public final class ExceptionTranslator {
  private ExceptionTranslator() {}

  public static AppException translate(Throwable t) {
    if (t instanceof AppException ae) return ae;

    if (t instanceof DomainException de) {
      return new AppException(CommonErrorCodes.CONFLICT, de.getMessage(), de,
          Map.of("domainException", de.getClass().getName()));
    }

    if (t instanceof InfraException ie) {
      ErrorCode code = mapInfraToCode(ie);
      Map<String, Serializable> attrs = new HashMap<>(ie.attributes());
      attrs.put("failureCategory", ie.category().name());
      attrs.put("failureReason", ie.reason().name());
      attrs.put("infraRetryable", ie.retryable());
      return new AppException(code, code.message(), ie, attrs);
    }

    return new AppException(CommonErrorCodes.SYSTEM_ERROR, CommonErrorCodes.SYSTEM_ERROR.message(), t,
        Map.of("exception", t.getClass().getName()));
  }

  private static ErrorCode mapInfraToCode(InfraException ie) {
    if (ie.category() == InfraException.FailureCategory.DB) {
      return switch (ie.reason()) {
        case DEADLOCK -> CommonErrorCodes.DB_DEADLOCK;
        case LOCK_TIMEOUT -> CommonErrorCodes.DB_LOCK_TIMEOUT;
        case CONSTRAINT_VIOLATION -> CommonErrorCodes.DB_CONSTRAINT;
        default -> CommonErrorCodes.DB_ERROR;
      };
    }

    return CommonErrorCodes.DEPENDENCY_FAILURE;
  }
}
