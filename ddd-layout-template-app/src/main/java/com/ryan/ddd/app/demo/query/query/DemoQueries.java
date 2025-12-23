package com.ryan.ddd.app.demo.query.query;

import com.ryan.ddd.app.demo.query.view.GetDemoDetailView;
import java.util.Optional;
import java.util.UUID;

public interface DemoQueries {
  Optional<GetDemoDetailView> findById(UUID id);
}
