package com.ryan.ddd.app.common;

import com.ryan.ddd.app.common.exception.AppException;
import com.ryan.ddd.app.common.exception.ExceptionTranslator;

/**
 * A {@link CommandHandler} decorator that translates domain/infra exceptions into {@link AppException}.
 *
 * <p>Use this at the application boundary so the adapter only needs to handle {@link AppException}.
 */
public final class TranslatingCommandHandler<C, R> implements CommandHandler<C, R> {

  private final CommandHandler<C, R> delegate;

  public TranslatingCommandHandler(CommandHandler<C, R> delegate) {
    if (delegate == null) {
      throw new IllegalArgumentException("delegate must not be null");
    }
    this.delegate = delegate;
  }

  @Override
  public R handle(C command) {
    try {
      return delegate.handle(command);
    } catch (Exception e) {
      throw ExceptionTranslator.translate(e);
    }
  }
}
