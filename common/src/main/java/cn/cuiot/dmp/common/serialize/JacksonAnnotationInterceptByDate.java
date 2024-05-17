package cn.cuiot.dmp.common.serialize;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.introspect.Annotated;
import com.fasterxml.jackson.databind.introspect.JacksonAnnotationIntrospector;
import java.io.IOException;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;

/**
 * @Author: lqj
 * @Date: 2020/1/16
 */
@Slf4j
public class JacksonAnnotationInterceptByDate extends JacksonAnnotationIntrospector {
    /**
     * 对Date 类型拦截，判断它是否有JsonFormat注解，有该注解且pattern不为空，则按照pattern解析时间
     *
     * @param a
     * @return
     */
    @Override
    public Object findSerializer(Annotated a) {
        try {
            if (a != null && a.getType() != null && a.getType().isTypeOrSubTypeOf(Date.class)) {
                JsonFormat jsonFormat = a.getAnnotation(JsonFormat.class);
                if (jsonFormat != null && StringUtils.isNotEmpty(jsonFormat.pattern())) {
                    return new JsonSerializer<Date>() {
                        @Override
                        public void serialize(Date date, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                            jsonGenerator.writeString(DateFormatUtils.format(date, jsonFormat.pattern()));
                        }
                    };
                }
            }
        } catch (Exception e) {
            log.error("JacksonAnnotationInterceptByDate error", e);
        }
        return super.findSerializer(a);
    }
}
