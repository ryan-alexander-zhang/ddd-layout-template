package com.ryan.ddd.app.demo.query.handler;

import com.ryan.ddd.app.demo.query.dto.GetDemoDetailQuery;
import com.ryan.ddd.app.demo.query.view.GetDemoDetailView;
import com.ryan.ddd.app.demo.query.query.DemoQueries;
import com.ryan.ddd.base.CommandHandler;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
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
