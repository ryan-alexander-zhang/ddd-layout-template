package com.ryan.ddd.infra.demo.persistence.read;

import com.ryan.ddd.app.demo.query.view.GetDemoDetailView;
import com.ryan.ddd.app.demo.query.query.DemoQueries;
import com.ryan.ddd.infra.demo.persistence.read.mapper.DemoQueryMapper;
import com.ryan.ddd.infra.demo.persistence.read.po.DemoQueryPO;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Repository;

@Repository
public class DemoQueriesImpl implements
    DemoQueries {

  private final DemoQueryMapper demoQueryMapper;

  public DemoQueriesImpl(DemoQueryMapper demoQueryMapper) {
    this.demoQueryMapper = demoQueryMapper;
  }

  @Override
  public Optional<GetDemoDetailView> findById(UUID id) {
    DemoQueryPO demoQueryPO = demoQueryMapper.selectById(id);
    if (demoQueryPO != null) {
      GetDemoDetailView getDemoDetailView = GetDemoDetailView.builder()
          .id(demoQueryPO.getId())
          .name(demoQueryPO.getName())
          .build();
      return Optional.of(getDemoDetailView);
    }
    return Optional.empty();
  }
}
