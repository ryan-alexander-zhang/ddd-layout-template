#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.demo.event;

import ${package}.domain.common.event.DomainEvent;
import java.util.UUID;
import lombok.Data;

@Data
public class DemoCreatedEvent implements DomainEvent {

  public final static String TYPE = "demo.created";

  private final UUID demoId;

  public DemoCreatedEvent(UUID demoId) {
    this.demoId = demoId;
  }

  public DemoCreatedEvent() {
    this.demoId = UUID.randomUUID();
  }

  @Override
  public String type() {
    return TYPE;
  }
}
