#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.infra.demo.persistence.write;

import ${package}.domain.demo.model.Demo;
import ${package}.domain.demo.repository.DemoRepository;
import ${package}.infra.demo.persistence.write.mapper.DemoMapper;
import ${package}.infra.demo.persistence.write.po.DemoPO;
import org.springframework.stereotype.Repository;

@Repository
public class DemoRepositoryImpl implements DemoRepository {

  private final DemoMapper demoMapper;

  public DemoRepositoryImpl(DemoMapper demoMapper) {
    this.demoMapper = demoMapper;
  }

  @Override
  public void save(Demo demo) {
    DemoPO demoPO = new DemoPO();
    demoPO.setId(demo.getId().getValue());
    demoPO.setName(demo.getName().getValue());
    demoPO.setCreatedAt(demo.getCreatedAt());
    demoMapper.insert(demoPO);
  }
}
