#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.infra.demo.persistence.write.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import ${package}.infra.demo.persistence.write.po.DemoPO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DemoMapper extends BaseMapper<DemoPO> {

}
