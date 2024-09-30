package cn.cuiot.dmp.externalapi.provider.rocketmq;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

/**
 * @Author: zc
 * @Date: 2024-09-24
 */
public interface SmsMsgChannel {
    /**
     * 短信-第三方数据推送
     */
    String SMS_PUSH_DATA_OUTPUT = "smsPushDataOutput";

    @Output(SMS_PUSH_DATA_OUTPUT)
    MessageChannel smsPushDataOutput();

}
