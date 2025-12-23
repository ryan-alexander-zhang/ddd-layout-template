package com.ryan.ddd.infra.demo.persistence.write.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ryan.ddd.infra.demo.persistence.write.po.DemoPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DemoMapper extends BaseMapper<DemoPO> {

}
