package cn.cuiot.dmp.externalapi.service.utils;

import cn.cuiot.dmp.externalapi.service.query.gw.push.base.DataItem;
import cn.cuiot.dmp.externalapi.service.query.gw.push.base.GwHead;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;

/**
 * 格物数据推送工具
 *
 * @Author: zc
 * @Date: 2024-09-12
 */
public class GwPushUtil {

    /**
     * 获取 `head` 对象，并根据传入的泛型解析数据
     *
     * @param data DeviceServiceOutParams 对象
     * @return 解析后的 head 对象
     */
    public static GwHead getHead(List<DataItem<Object>> data) {
        for (DataItem<Object> item : data) {
            if (Objects.equals(item.getKey(), "head")) {
                Object value = item.getValue();

                if (Objects.nonNull(value)) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    return objectMapper.convertValue(value, GwHead.class);
                }
            }
        }

        return null;
    }

    /**
     * 获取 `body` 对象，并根据传入的泛型解析数据
     *
     * @param data DeviceServiceOutParams 对象
     * @param bodyType  需要解析为的泛型类型
     * @param <T>       泛型类型
     * @return 解析后的 body 对象
     */
    public static <T> T getBody(List<DataItem<Object>> data, Class<T> bodyType) {
        for (DataItem<Object> item : data) {
            if (Objects.equals(item.getKey(), "body")) {
                Object value = item.getValue();

                if (Objects.nonNull(value)) {
                    ObjectMapper objectMapper = new ObjectMapper();
                    return objectMapper.convertValue(value, bodyType);
                }
            }
        }

        return null;
    }
}
