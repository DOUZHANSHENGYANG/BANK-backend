package xyz.douzhan.bank.handler;

import cn.hutool.crypto.SmUtil;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;
import xyz.douzhan.bank.utils.CypherUtils;
import xyz.douzhan.bank.utils.JWTUtils;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;

/**
 * 一些声明信息
 * Description:实现重要信息加密存储
 * date: 2023/11/28 12:10
 *
 * @author 斗战圣洋
 * @since JDK 17
 */

public class EncodeTypeHandler implements TypeHandler<String> {


    @Override
    public void setParameter(PreparedStatement ps, int i, String parameter, JdbcType jdbcType) throws SQLException {
        String encrypt = CypherUtils.encryptSM4(parameter);
        ps.setString(i,encrypt);
    }

    /**
     * 存储数据加密
     * @param rs
     * @param columnName
     * @return
     * @throws SQLException
     */
    @Override
    public String getResult(ResultSet rs, String columnName) throws SQLException {
        return CypherUtils.decryptSM4(rs.getString(columnName));
    }

    /**
     * 查询数据解密
     * @param rs
     * @param columnIndex
     * @return
     * @throws SQLException
     */
    @Override
    public String getResult(ResultSet rs, int columnIndex) throws SQLException {
        return CypherUtils.decryptSM4(rs.getString(columnIndex));
    }

    @Override
    public String getResult(CallableStatement cs, int columnIndex) throws SQLException {
        return CypherUtils.decryptSM4(cs.getString(columnIndex));
    }
}
