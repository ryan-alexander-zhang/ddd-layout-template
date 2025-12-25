#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.demo.model;

public final class DemoDraft {

  private final Demo demo;

  DemoDraft(Demo demo) {
    this.demo = demo;
  }

  public void changeName(DemoName name) {
    demo.changeName(name);
  }

  public Demo submit() {
    demo.submit();
    return this.demo;
  }
}
