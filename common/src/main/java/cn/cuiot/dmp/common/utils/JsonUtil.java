package cn.cuiot.dmp.common.utils;

import cn.cuiot.dmp.common.serialize.LongTypeJsonSerializer;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Json转换工具
 * @author: wuyongchong
 * @date: 2024/5/8 8:50
 */
public final class JsonUtil {

    private JsonUtil() throws IllegalAccessException {
        throw new IllegalAccessException("can't get the instance of util class ");
    }

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper().findAndRegisterModules();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        // 取消默认转换timestamps形式
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        // 统一日期格式yyyy-MM-dd HH:mm:ss
        objectMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));
        objectMapper.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        SimpleModule module = new SimpleModule();
        //解决Long 长度丢失问题
        module.addSerializer(Long.TYPE, new LongTypeJsonSerializer());
        module.addSerializer(Long.class, new LongTypeJsonSerializer());
        objectMapper.registerModule(module);
    }

    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    public static <T> T readValue(String src, Class<T> clazz) {
        if (StringUtils.isEmpty(src)) {
            return null;
        }
        T t = null;
        try {
            t = objectMapper.readValue(src, clazz);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return t;
    }

    public static <T> T readValue(String src, TypeReference<T> typeReference) {
        if (StringUtils.isEmpty(src)) {
            return null;
        }
        T t = null;
        try {
            t = objectMapper.readValue(src, typeReference);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return t;
    }

    public static String writeValueAsString(Object obj) {
        if (null == obj) {
            return null;
        }
        String src = null;
        try {
            src = objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        }
        return src;
    }
}