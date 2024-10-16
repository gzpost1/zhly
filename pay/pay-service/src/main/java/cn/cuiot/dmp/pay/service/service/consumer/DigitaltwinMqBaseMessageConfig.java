package cn.cuiot.dmp.pay.service.service.consumer;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(value = {PayMsgBaseChannel.class})
public class DigitaltwinMqBaseMessageConfig {
}
