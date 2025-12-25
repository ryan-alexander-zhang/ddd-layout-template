#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.infra.demo.persistence.read.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ${package}.infra.demo.persistence.read.po.DemoQueryPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DemoQueryMapper extends BaseMapper<DemoQueryPO> {

}
