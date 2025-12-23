package com.ryan.ddd.base;

public interface CommandHandler<C, R> {

  R handle(C command);
}
