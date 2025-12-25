#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.demo.model;

import java.util.UUID;
import lombok.Getter;

@Getter
public final class DemoId {

  private final UUID value;

  private DemoId(UUID value) {
    this.value = value;
  }

  public static DemoId newId() {
    return new DemoId(UUID.randomUUID());
  }
}
