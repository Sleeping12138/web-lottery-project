package com.test.lotterysystem.Dao.TypeHandler;

import cn.hutool.crypto.SecureUtil;
import com.test.lotterysystem.Dao.DaoObject.Encrypt;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedJdbcTypes;
import org.apache.ibatis.type.MappedTypes;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
@MappedTypes(Encrypt.class)
@MappedJdbcTypes(JdbcType.VARCHAR)
public class EncryptTypeHandler extends BaseTypeHandler<Encrypt> {
    private final byte[] key = "123456789abcdefg".getBytes(StandardCharsets.UTF_8);

    // 往Mybatis中存储数据的时候进行转换
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Encrypt parameter, JdbcType jdbcType) throws SQLException {
        if(parameter.getValue()==null){
            ps.setString(i,null);
            return;
        }

        System.out.println("加密的内容：" + parameter.getValue());
        ps.setString(i,SecureUtil.aes(key).encryptHex(parameter.getValue()));
    }

    // 从Mybatis中获取数据的时候进行的转换
    @Override
    public Encrypt getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return new Encrypt(SecureUtil.aes(key).decryptStr(rs.getString(columnName)));
    }

    @Override
    public Encrypt getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return new Encrypt(SecureUtil.aes(key).decryptStr(rs.getString(columnIndex)));
    }

    @Override
    public Encrypt getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return new Encrypt(SecureUtil.aes(key).decryptStr(cs.getString(columnIndex)));
    }
}
