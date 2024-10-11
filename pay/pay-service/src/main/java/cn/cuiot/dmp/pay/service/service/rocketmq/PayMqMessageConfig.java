package cn.cuiot.dmp.pay.service.service.rocketmq;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(value = {PayChannel.class})
public class PayMqMessageConfig {
}
