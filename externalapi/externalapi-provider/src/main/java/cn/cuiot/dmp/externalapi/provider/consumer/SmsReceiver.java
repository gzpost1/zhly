package cn.cuiot.dmp.externalapi.provider.consumer;

import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.externalapi.provider.consumer.config.SmsMsgBaseChannel;
import cn.cuiot.dmp.sms.query.SmsPushDataQuery;
import cn.cuiot.dmp.sms.service.SmsSendRecordService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 短信 mq消防
 *
 * @Author: zc
 * @Date: 2024-09-24
 */
@Slf4j
@Component
public class SmsReceiver {

    @Autowired
    private SmsSendRecordService sendRecordService;

    /**
     * 数据推送
     *
     * @param message 参数
     */
    @StreamListener(SmsMsgBaseChannel.SMS_PUSH_DATA_INPUT)
    public void receiveDeviceInput(@Payload Message<List<LinkedHashMap<String, Object>>> message) {
        log.info("短信【第三方推送】入库操作..........." + JsonUtil.writeValueAsString(message.getPayload()));

        List<SmsPushDataQuery> collect = message.getPayload().stream().map(item -> {
            if (MapUtils.isNotEmpty(item)) {
                // 将 LinkedHashMap 转换为 SmsPushDataQuery
                return JsonUtil.readValue(JsonUtil.writeValueAsString(item), SmsPushDataQuery.class);
            }
            return null;
        }).filter(Objects::nonNull).collect(Collectors.toList());

        if (CollectionUtils.isNotEmpty(collect)) {
            sendRecordService.pushSendRecord(collect);
        }
    }
}
