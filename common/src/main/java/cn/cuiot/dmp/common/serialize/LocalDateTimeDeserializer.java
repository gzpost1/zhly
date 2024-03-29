package cn.cuiot.dmp.common.serialize;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * @author jiangze
 * @classname LocalDateTimeDeserializer
 * @description 时间戳序列化转换类
 * @date 2020-07-23
 */
public class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {

    /**
     * 东八区时区
     */
    private String zoneOffsetId = "+8";

    @Override
    public LocalDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        long timestamp = p.getValueAsLong();
        if (timestamp == 0) {
            return null;
        }
        Instant instant = Instant.ofEpochMilli(timestamp);
        return LocalDateTime.ofInstant(instant, ZoneOffset.of(zoneOffsetId));
    }

}
