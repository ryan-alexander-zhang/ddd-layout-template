package com.ryan.ddd.infra.common.persistence;

import java.nio.ByteBuffer;
import java.util.UUID;

// TODO temp
public final class UuidBytes {

  private UuidBytes() {
  }

  public static byte[] toBytes(UUID uuid) {
    ByteBuffer bb = ByteBuffer.allocate(16);
    bb.putLong(uuid.getMostSignificantBits());
    bb.putLong(uuid.getLeastSignificantBits());
    return bb.array();
  }

  public static UUID fromBytes(byte[] bytes) {
    ByteBuffer bb = ByteBuffer.wrap(bytes);
    long high = bb.getLong();
    long low = bb.getLong();
    return new UUID(high, low);
  }
}
