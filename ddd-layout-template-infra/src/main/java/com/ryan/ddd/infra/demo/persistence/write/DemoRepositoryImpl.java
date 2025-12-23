package com.ryan.ddd.infra.demo.persistence.write;

import com.ryan.ddd.domain.demo.model.Demo;
import com.ryan.ddd.domain.demo.repository.DemoRepository;
import com.ryan.ddd.infra.demo.persistence.write.mapper.DemoMapper;
import com.ryan.ddd.infra.demo.persistence.write.po.DemoPO;
import java.util.UUID;
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
