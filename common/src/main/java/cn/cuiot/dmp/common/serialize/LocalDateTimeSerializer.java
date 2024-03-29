package cn.cuiot.dmp.common.serialize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author jiangze
 * @classname LocalDateTimeSerializer
 * @description LocalDateTime序列化转换类
 * @date 2020-07-23
 */
public class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {

    /**
     * 东八区时区
     */
    private String zoneOffsetId = "+8";

    @Override
    public void serialize(LocalDateTime localDateTime, JsonGenerator jsonGenerator,
        SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeNumber(localDateTime.toInstant(ZoneOffset.of(zoneOffsetId)).toEpochMilli());
    }

}
