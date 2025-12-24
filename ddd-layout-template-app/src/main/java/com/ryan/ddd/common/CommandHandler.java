package com.ryan.ddd.common;

public interface CommandHandler<C, R> {

  R handle(C command);
}
