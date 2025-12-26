package com.ryan.ddd.adapter.common;

import io.opentelemetry.api.baggage.Baggage;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestIdFilter extends OncePerRequestFilter {

  private static final String HEADER = "X-Request-ID";
  private static final String MDC_KEY = "request_id";

  // Stable keys, not the header name.
  private static final String BAGGAGE_KEY = "request.id";
  private static final AttributeKey<String> ATTR = AttributeKey.stringKey(BAGGAGE_KEY);

  private static final Pattern UUID_PATTERN =
      Pattern.compile(
          "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
  private static final int MAX_LEN = 64;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {

    String rid = normalizeOrGenerate(request.getHeader(HEADER));

    MDC.put(MDC_KEY, rid);
    try (Scope scope = Baggage.current().toBuilder().put(BAGGAGE_KEY, rid).build().makeCurrent()) {
      response.setHeader(HEADER, rid);

      Span span = Span.current();
      if (span != null) {
        span.setAttribute(ATTR, rid);
      }

      filterChain.doFilter(request, response);
    } finally {
      MDC.remove(MDC_KEY);
    }
  }

  private static String normalizeOrGenerate(String candidate) {
    if (candidate == null) {
      return UUID.randomUUID().toString();
    }

    String v = candidate.trim();
    if (v.isEmpty() || v.length() > MAX_LEN) {
      return UUID.randomUUID().toString();
    }

    // If you want to allow non-UUID request ids, replace with a safe regex allowlist.
    if (!UUID_PATTERN.matcher(v).matches()) {
      return UUID.randomUUID().toString();
    }

    return v;
  }
}
