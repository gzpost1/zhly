package cn.cuiot.dmp.common.serialize;


import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.google.common.collect.Lists;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import org.apache.commons.lang3.StringUtils;

/**
 * Long序列化器
 *
 * @author wuyongchong
 * @date 2019/12/18
 */
public class LongTypeJsonSerializer extends JsonSerializer<Long> {

    public static final LongTypeJsonSerializer INSTANCE = new LongTypeJsonSerializer();

    private static final List<String> NONE_LIST;

    static {
        NONE_LIST = Lists.newArrayList();
        NONE_LIST.add("total");
        NONE_LIST.add("size");
        NONE_LIST.add("current");
        NONE_LIST.add("pages");
    }

    public LongTypeJsonSerializer() {
    }

    @Override
    public void serialize(Long val, JsonGenerator jsonGenerator,
            SerializerProvider serializerProvider) throws IOException {
        if (!Objects.isNull(val)) {
            String currentName = null;
            if (null != jsonGenerator.getOutputContext()) {
                currentName = jsonGenerator.getOutputContext().getCurrentName();
            }
            if (StringUtils.isNotBlank(currentName) && NONE_LIST.contains(currentName)) {
                jsonGenerator.writeNumber(val);
            } else {
                jsonGenerator.writeString(val.toString());
            }
        }
    }

}