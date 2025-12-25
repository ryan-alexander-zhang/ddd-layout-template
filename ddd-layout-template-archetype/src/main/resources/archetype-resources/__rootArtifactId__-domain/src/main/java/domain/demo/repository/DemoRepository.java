#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.demo.repository;

import ${package}.domain.demo.model.Demo;

public interface DemoRepository {

  void save(Demo demo);
}
