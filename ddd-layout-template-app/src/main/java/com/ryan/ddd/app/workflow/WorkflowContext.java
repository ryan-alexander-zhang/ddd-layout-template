package com.ryan.ddd.app.workflow;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class WorkflowContext {
  private final Map<String, Object> values = new HashMap<>();

  public void put(String key, Object value) {
    values.put(key, value);
  }

  public Object get(String key) {
    return values.get(key);
  }

  public Map<String, Object> snapshot() {
    return Collections.unmodifiableMap(values);
  }
}
