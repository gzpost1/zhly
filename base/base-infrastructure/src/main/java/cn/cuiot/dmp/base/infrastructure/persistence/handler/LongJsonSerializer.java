package cn.cuiot.dmp.base.infrastructure.persistence.handler;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.Objects;

/**
 * @Description 防止long类型前端截断
 * @Date 2024/4/23 17:09
 * @Created by libo
 */
public class LongJsonSerializer extends JsonSerializer<Long> {
    public LongJsonSerializer() {
    }

    @Override
    public void serialize(Long value, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        if (!Objects.isNull(value)) {
            jsonGenerator.writeString(value.toString());
        }

    }
}