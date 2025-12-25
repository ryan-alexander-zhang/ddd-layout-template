#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.app.demo.query.handler;

import ${package}.app.demo.query.dto.GetDemoDetailQuery;
import ${package}.app.demo.query.view.GetDemoDetailView;
import ${package}.app.demo.query.query.DemoQueries;
import ${package}.common.CommandHandler;
import java.util.Optional;

public class GetDemoDetailQueryHandler implements CommandHandler<GetDemoDetailQuery, Optional<GetDemoDetailView>> {

  private final DemoQueries demoQueries;

  public GetDemoDetailQueryHandler(DemoQueries demoQueries) {
    this.demoQueries = demoQueries;
  }

  @Override
  public Optional<GetDemoDetailView> handle(GetDemoDetailQuery query) {
    return demoQueries.findById(query.getId());
  }
}
