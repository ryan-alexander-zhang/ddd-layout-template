package com.ryan.ddd.app.cqrs;

public interface QueryHandler<Q extends Query, R> {
  R handle(Q query);
}
