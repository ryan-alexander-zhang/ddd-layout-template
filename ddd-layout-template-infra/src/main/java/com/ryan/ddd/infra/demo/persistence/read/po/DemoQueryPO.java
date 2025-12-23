package com.ryan.ddd.infra.demo.persistence.read.po;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@TableName("demo")
public class DemoQueryPO {
  @TableId
  private UUID id;
  private String name;
}
