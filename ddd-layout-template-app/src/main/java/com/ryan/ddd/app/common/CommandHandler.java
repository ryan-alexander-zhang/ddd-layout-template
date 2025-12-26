package com.ryan.ddd.app.common;

public interface CommandHandler<C, R> {

  R handle(C command);
}
