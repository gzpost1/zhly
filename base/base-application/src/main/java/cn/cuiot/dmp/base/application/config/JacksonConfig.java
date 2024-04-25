package cn.cuiot.dmp.base.application.config;

import cn.cuiot.dmp.common.deserializer.StringToDateDeserializer;
import cn.cuiot.dmp.common.serialize.JacksonAnnotationInterceptByDate;
import cn.cuiot.dmp.common.serialize.LongTypeJsonSerializer;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

/**
 * 前端Jackson序列化配置
 *
 * @author: wuyongchong
 * @date: 2024/4/25 17:18
 */
@Configuration
public class JacksonConfig {

    private final ApplicationContext applicationContext;

    public JacksonConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public Jackson2ObjectMapperBuilder jackson2ObjectMapperBuilder(
            List<Jackson2ObjectMapperBuilderCustomizer> customizers) {

        Jackson2ObjectMapperBuilder mapperBuilder = new Jackson2ObjectMapperBuilder();

        mapperBuilder.applicationContext(this.applicationContext);

        Iterator customizersIterator = customizers.iterator();
        while (customizersIterator.hasNext()) {
            Jackson2ObjectMapperBuilderCustomizer customizer =
                    (Jackson2ObjectMapperBuilderCustomizer) customizersIterator
                            .next();
            customizer.customize(mapperBuilder);
        }
        //解决Long 长度丢失问题
        mapperBuilder.serializerByType(Long.class, LongTypeJsonSerializer.INSTANCE)
                .serializerByType(Long.TYPE, LongTypeJsonSerializer.INSTANCE);
        //解决时间入参转换Date问题
        mapperBuilder.deserializerByType(Date.class, new StringToDateDeserializer());
        mapperBuilder.annotationIntrospector(new JacksonAnnotationInterceptByDate());
        return mapperBuilder;
    }

}
