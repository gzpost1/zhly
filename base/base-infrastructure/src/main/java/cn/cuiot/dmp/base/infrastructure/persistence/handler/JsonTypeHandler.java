package cn.cuiot.dmp.base.infrastructure.persistence.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @Description 单对象映射
 * @Date 2024/4/23 17:46
 * @Created by libo
 */
public class JsonTypeHandler extends BaseTypeHandler<Object> {
    private static ObjectMapper objectMapper = (new ObjectMapper()).findAndRegisterModules();
    private Class<Object> clazz;

    public JsonTypeHandler(Class<Object> type) {
        if (type == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        } else {
            this.clazz = type;
        }
    }

    private Object parse(String json) {
        try {
            return json != null && json.length() != 0 ? objectMapper.readValue(json, this.clazz) : null;
        } catch (IOException var3) {
            throw new RuntimeException(var3);
        }
    }

    private String toJsonString(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException var3) {
            throw new RuntimeException(var3);
        }
    }

    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        return this.parse(json);
    }

    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return this.parse(rs.getString(columnIndex));
    }

    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return this.parse(cs.getString(columnIndex));
    }

    public void setNonNullParameter(PreparedStatement ps, int columnIndex, Object parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(columnIndex, this.toJsonString(parameter));
    }

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.TYPE, new LongJsonSerializer());
        module.addSerializer(Long.class, new LongJsonSerializer());
        objectMapper.registerModule(module);
    }
}
