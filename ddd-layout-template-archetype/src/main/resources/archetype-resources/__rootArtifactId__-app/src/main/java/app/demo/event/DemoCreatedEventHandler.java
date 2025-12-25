#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.app.demo.event;

import ${package}.common.event.EventHandler;
import ${package}.domain.demo.event.DemoCreatedEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DemoCreatedEventHandler implements EventHandler<DemoCreatedEvent> {

  @Override
  public void handler(DemoCreatedEvent event) {
    log.info("DemoCreatedEventHandler event handled. id: {}", event.getDemoId().toString());
  }

  @Override
  public String type() {
    return DemoCreatedEvent.TYPE;
  }

  @Override
  public Class<DemoCreatedEvent> payloadClass() {
    return DemoCreatedEvent.class;
  }
}
