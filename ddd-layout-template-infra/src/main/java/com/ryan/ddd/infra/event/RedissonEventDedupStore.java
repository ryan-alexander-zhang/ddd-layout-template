package com.ryan.ddd.infra.event;

import com.ryan.ddd.app.event.EventDedupStore;
import java.time.Duration;
import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;

public class RedissonEventDedupStore implements EventDedupStore {
  private final RedissonClient redissonClient;

  public RedissonEventDedupStore(RedissonClient redissonClient) {
    this.redissonClient = redissonClient;
  }

  @Override
  public boolean markIfNotProcessed(String eventId, Duration ttl) {
    RBucket<String> bucket = redissonClient.getBucket("event:dedup:" + eventId);
    return bucket.trySet("1", ttl);
  }
}
