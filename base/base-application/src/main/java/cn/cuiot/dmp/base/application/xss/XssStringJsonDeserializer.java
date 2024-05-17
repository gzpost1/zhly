package cn.cuiot.dmp.base.application.xss;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import java.io.IOException;
import org.springframework.web.util.HtmlUtils;

/**
 * 基于xss的JsonDeserializer
 *
 * @author: wuyongchong
 * @date: 2024/5/15 16:25
 */
public class XssStringJsonDeserializer extends JsonDeserializer<String> {

    @Override
    public Class<String> handledType() {
        return String.class;
    }

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException {
        return HtmlUtils.htmlEscape(jsonParser.getValueAsString());
    }
}