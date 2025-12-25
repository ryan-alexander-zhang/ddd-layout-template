#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.domain.demo.model;

import java.time.Instant;
import java.util.Objects;
import lombok.Getter;

@Getter
public final class Demo {

  private final DemoId id;
  private DemoName name;
  private DemoStatus status;
  private final Instant createdAt;

  private Demo(DemoId id, DemoName name, DemoStatus status) {
    this.id = id;
    this.name = name;
    this.status = status;
    createdAt = Instant.now();
  }

  public static Demo create(DemoName name) {
    DemoId demoId = DemoId.newId();
    return new Demo(demoId, name, DemoStatus.SUBMITTED);
  }

  public static Demo createDraft() {
    return new Demo(DemoId.newId(), null, DemoStatus.DRAFT);
  }

  public DemoDraft draft() {
    ensureDraft();
    return new DemoDraft(this);
  }

  private void ensureDraft() {
    if (this.status != DemoStatus.DRAFT) {
      throw new IllegalStateException("demo is not draft");
    }
  }

  void changeName(DemoName name) {
    ensureDraft();
    this.name = Objects.requireNonNull(name, "demo name is required");
  }

  void submit() {
    ensureDraft();
    if (this.name == null) {
      throw new IllegalStateException("demo name is required");
    }
    this.status = DemoStatus.SUBMITTED;
  }
}
