#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.demo.model;

import java.util.Objects;
import lombok.Getter;

@Getter
public final class DemoName {

  private final String value;

  private DemoName(String value) {
    this.value = value;
  }

  public static DemoName of(String value) {
    String v = Objects.requireNonNull(value, "name").trim();
    if (v.isEmpty()) {
      throw new IllegalArgumentException("name must not be blank");
    }
    return new DemoName(v);
  }
}
