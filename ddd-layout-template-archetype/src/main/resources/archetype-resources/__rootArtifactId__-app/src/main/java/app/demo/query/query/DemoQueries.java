#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.app.demo.query.query;

import ${package}.app.demo.query.view.GetDemoDetailView;
import java.util.Optional;
import java.util.UUID;

public interface DemoQueries {
  Optional<GetDemoDetailView> findById(UUID id);
}
