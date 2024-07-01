package cn.cuiot.dmp.digitaltwin.provider.rocketmq;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(value = {DigitaltwinMsgChannel.class})
public class DigitaltwinMqMessageConfig {
}
