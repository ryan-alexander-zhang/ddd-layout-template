package com.ryan.ddd.infra.demo.persistence.write.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.time.Instant;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("demo")
public class DemoPO {

  @TableId
  private UUID id;
  private String name;
  private Instant createdAt;
}
