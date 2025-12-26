package com.ryan.ddd.app.demo.query.handler;

import com.ryan.ddd.app.common.CommandHandler;
import com.ryan.ddd.app.demo.query.dto.GetDemoDetailQuery;
import com.ryan.ddd.app.demo.query.query.DemoQueries;
import com.ryan.ddd.app.demo.query.view.GetDemoDetailView;
import java.util.Optional;

public class GetDemoDetailQueryHandler implements
    CommandHandler<GetDemoDetailQuery, Optional<GetDemoDetailView>> {

  private final DemoQueries demoQueries;

  public GetDemoDetailQueryHandler(DemoQueries demoQueries) {
    this.demoQueries = demoQueries;
  }

  @Override
  public Optional<GetDemoDetailView> handle(GetDemoDetailQuery query) {
    return demoQueries.findById(query.getId());
  }
}
