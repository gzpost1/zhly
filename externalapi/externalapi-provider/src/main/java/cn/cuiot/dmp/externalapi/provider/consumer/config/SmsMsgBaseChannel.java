package cn.cuiot.dmp.externalapi.provider.consumer.config;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 接收消息队列名称
 *
 * @Author: zc
 * @Date: 2024-06-14
 */
public interface SmsMsgBaseChannel {
    /**
     * 短信-第三方推送数据
     */
    String SMS_PUSH_DATA_INPUT = "smsPushDataInput";

    @Input(SMS_PUSH_DATA_INPUT)
    SubscribableChannel smsPushDataInput();

}
