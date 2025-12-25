#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.infra.common.outbox;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class OutboxEventTypeRegistry {
  private final Map<String, Class<?>> types = new ConcurrentHashMap<>();

  public void register(String type, Class<?> clazz) {
    types.put(type, clazz);
  }

  public Class<?> required(String type) {
    Class<?> c = types.get(type);
    if (c == null) throw new IllegalArgumentException("Unknown outbox event type: " + type);
    return c;
  }
}
