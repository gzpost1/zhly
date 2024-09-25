package cn.cuiot.dmp.externalapi.provider.consumer;

import cn.cuiot.dmp.common.utils.JsonUtil;
import cn.cuiot.dmp.externalapi.provider.consumer.config.SmsMsgBaseChannel;
import cn.cuiot.dmp.sms.query.SmsPushDataQuery;
import cn.cuiot.dmp.sms.service.SmsSendRecordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

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
     * @param query 参数
     */
    @StreamListener(SmsMsgBaseChannel.SMS_PUSH_DATA_INPUT)
    public void receiveDeviceInput(@Payload SmsPushDataQuery query) {
        log.info("短信【第三方推送】入库操作" + JsonUtil.writeValueAsString(query));
        sendRecordService.sendRecord(query);
    }
}
