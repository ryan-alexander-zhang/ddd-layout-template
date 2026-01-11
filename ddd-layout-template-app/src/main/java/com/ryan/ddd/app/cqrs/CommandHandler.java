package com.ryan.ddd.app.cqrs;

public interface CommandHandler<C extends Command, R> {
  R handle(C command);
}
