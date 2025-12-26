package com.ryan.ddd.infra.common.exception;

import com.ryan.ddd.app.common.exception.InfraException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public final class MyBatisPlusDbExceptionClassifier {

  private MyBatisPlusDbExceptionClassifier() {
  }

  public static DatabaseException classify(String op, Throwable t) {
    SQLException se = findSqlException(t);

    Map<String, Serializable> attrs = new HashMap<>();
    attrs.put("op", op);
    attrs.put("exception", t.getClass().getName());

    if (se != null) {
      int errno = se.getErrorCode();
      String sqlState = se.getSQLState();
      attrs.put("errno", errno);
      attrs.put("sqlState", sqlState);

      InfraException.FailureReason reason = mapMySql(errno, sqlState);
      boolean retryable = isRetryable(reason);

      return new DatabaseException(reason, retryable, "DB failure on " + op, t, attrs);
    }

    return new DatabaseException(InfraException.FailureReason.UNKNOWN, false, "DB failure on " + op,
        t, attrs);
  }

  private static SQLException findSqlException(Throwable t) {
    Throwable cur = t;
    int guard = 0;
    while (cur != null && guard++ < 20) {
      if (cur instanceof SQLException se) {
        return se;
      }
      cur = cur.getCause();
    }
    return null;
  }

  private static InfraException.FailureReason mapMySql(int errno, String sqlState) {
    // MySQL common errno：
    // 1213 deadlock
    // 1205 lock wait timeout
    // 1062 duplicate key
    if (errno == 1213) {
      return InfraException.FailureReason.DEADLOCK;
    }
    if (errno == 1205) {
      return InfraException.FailureReason.LOCK_TIMEOUT;
    }
    if (errno == 1062) {
      return InfraException.FailureReason.CONSTRAINT_VIOLATION;
    }

    // SQLState 40001 serialization, deadlock
    if ("40001".equals(sqlState)) {
      return InfraException.FailureReason.DEADLOCK;
    }

    return InfraException.FailureReason.UNKNOWN;
  }

  private static boolean isRetryable(InfraException.FailureReason reason) {
    return reason == InfraException.FailureReason.DEADLOCK
        || reason == InfraException.FailureReason.LOCK_TIMEOUT
        || reason == InfraException.FailureReason.TIMEOUT
        || reason == InfraException.FailureReason.CONNECTION
        || reason == InfraException.FailureReason.UNAVAILABLE
        || reason == InfraException.FailureReason.THROTTLED;
  }
}
