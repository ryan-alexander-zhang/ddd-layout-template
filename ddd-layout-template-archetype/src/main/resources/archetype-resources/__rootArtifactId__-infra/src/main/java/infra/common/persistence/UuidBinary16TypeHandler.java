#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.infra.common.persistence;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;

@MappedTypes(UUID.class)
@MappedJdbcTypes(JdbcType.BINARY)
public class UuidBinary16TypeHandler extends BaseTypeHandler<UUID> {

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, UUID parameter, JdbcType jdbcType)
      throws SQLException {
    ps.setBytes(i, UuidBytes.toBytes(parameter));
  }

  @Override
  public UUID getNullableResult(ResultSet rs, String columnName) throws SQLException {
    byte[] b = rs.getBytes(columnName);
    return b == null ? null : UuidBytes.fromBytes(b);
  }

  @Override
  public UUID getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    byte[] b = rs.getBytes(columnIndex);
    return b == null ? null : UuidBytes.fromBytes(b);
  }

  @Override
  public UUID getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    byte[] b = cs.getBytes(columnIndex);
    return b == null ? null : UuidBytes.fromBytes(b);
  }
}
