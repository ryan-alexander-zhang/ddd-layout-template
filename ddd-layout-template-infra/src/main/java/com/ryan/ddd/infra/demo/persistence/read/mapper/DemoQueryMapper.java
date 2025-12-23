package com.ryan.ddd.infra.demo.persistence.read.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryan.ddd.infra.demo.persistence.read.po.DemoQueryPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DemoQueryMapper extends BaseMapper<DemoQueryPO> {

}
