package com.ryan.ddd.app.workflow;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public final class WorkflowDefinition {
  private final String name;
  private final Map<String, WorkflowNode> nodes;
  private final Map<String, Set<String>> dependencies;

  private WorkflowDefinition(String name, Map<String, WorkflowNode> nodes,
      Map<String, Set<String>> dependencies) {
    this.name = Objects.requireNonNull(name, "name");
    this.nodes = nodes;
    this.dependencies = dependencies;
  }

  public String name() {
    return name;
  }

  public Map<String, WorkflowNode> nodes() {
    return Collections.unmodifiableMap(nodes);
  }

  public Map<String, Set<String>> dependencies() {
    Map<String, Set<String>> snapshot = new LinkedHashMap<>();
    dependencies.forEach((key, value) -> snapshot.put(key, Collections.unmodifiableSet(value)));
    return snapshot;
  }

  public static Builder builder(String name) {
    return new Builder(name);
  }

  public static final class Builder {
    private final String name;
    private final Map<String, WorkflowNode> nodes = new LinkedHashMap<>();
    private final Map<String, Set<String>> dependencies = new LinkedHashMap<>();

    private Builder(String name) {
      this.name = Objects.requireNonNull(name, "name");
    }

    public Builder addNode(WorkflowNode node) {
      nodes.put(node.id(), node);
      dependencies.putIfAbsent(node.id(), new LinkedHashSet<>());
      return this;
    }

    public Builder dependsOn(String nodeId, String dependsOnId) {
      dependencies.computeIfAbsent(nodeId, key -> new LinkedHashSet<>()).add(dependsOnId);
      return this;
    }

    public WorkflowDefinition build() {
      return new WorkflowDefinition(name, new LinkedHashMap<>(nodes),
          new LinkedHashMap<>(dependencies));
    }
  }
}
