package com.ryan.ddd.app.common.exception;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.ryan.ddd.app.common.error.CommonErrorCodes;
import com.ryan.ddd.app.common.error.InfraErrorCodes;
import com.ryan.ddd.domain.common.exception.DomainException;
import java.util.Map;
import org.junit.jupiter.api.Test;

class ExceptionTranslatorTest {

  @Test
  void translate_domainException_toConflict() {
    AppException ex = ExceptionTranslator.translate(new DomainException("bad state"));
    assertEquals(CommonErrorCodes.CONFLICT.code(), ex.getErrorCode().code());
  }

  @Test
  void translate_infraDeadlock_toDbDeadlock() {
    InfraException infra = new InfraException(
        InfraException.FailureCategory.DB,
        InfraException.FailureReason.DEADLOCK,
        true,
        "deadlock",
        null,
        Map.of("op", "demo.insert")) {
    };

    AppException ex = ExceptionTranslator.translate(infra);
    assertEquals(InfraErrorCodes.DB_DEADLOCK.code(), ex.getErrorCode().code());
  }

  @Test
  void translate_serde_toSerdeError() {
    InfraException infra = new InfraException(
        InfraException.FailureCategory.SERDE,
        InfraException.FailureReason.SERDE,
        false,
        "serde",
        null,
        Map.of()) {
    };

    AppException ex = ExceptionTranslator.translate(infra);
    assertEquals(InfraErrorCodes.SERDE_ERROR.code(), ex.getErrorCode().code());
  }
}

