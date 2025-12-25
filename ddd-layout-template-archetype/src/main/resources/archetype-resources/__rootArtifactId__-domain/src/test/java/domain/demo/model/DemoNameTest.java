#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.demo.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class DemoNameTest {

  @Test
  void of() {
    DemoName demoName = DemoName.of("test-name");
    assertEquals("test-name", demoName.getValue());
  }
}