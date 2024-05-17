package cn.cuiot.dmp.base.infrastructure.persistence.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.apache.ibatis.type.JdbcType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Description 字符串装list对象
 * @Date 2024/4/23 16:51
 * @Created by libo
 */
public class ListObjectJsonTypeHandler {
    private static final Logger log = LoggerFactory.getLogger(ListObjectJsonTypeHandler.class);
    private static ObjectMapper objectMapper = (new ObjectMapper()).findAndRegisterModules();
    private Class<Object> clazz;

    public ListObjectJsonTypeHandler(Class<Object> clazz) {
        if (clazz == null) {
            throw new IllegalArgumentException("Type argument cannot be null");
        } else {
            this.clazz = clazz;
        }
    }

    private Object parse(String json) {
        try {
            if (json != null && json.length() != 0) {
                JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, this.clazz);
                return objectMapper.readValue(json, javaType);
            } else {
                return null;
            }
        } catch (IOException var3) {
            log.error("deserialization failed {}", json, var3);
            throw new RuntimeException(var3);
        }
    }

    private String toJsonString(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException var3) {
            log.error("serialization failed {}", object, var3);
            throw new RuntimeException(var3);
        }
    }

    public void setNonNullParameter(PreparedStatement ps, int columnIndex, Object parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(columnIndex, this.toJsonString(parameter));
    }

    public Object getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return this.parse(rs.getString(columnName));
    }

    public Object getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return this.parse(rs.getString(columnIndex));
    }

    public Object getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return this.parse(cs.getString(columnIndex));
    }

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        SimpleModule module = new SimpleModule();
        module.addSerializer(Long.TYPE, new LongJsonSerializer());
        module.addSerializer(Long.class, new LongJsonSerializer());
        objectMapper.registerModule(module);
    }
}
