package cn.cuiot.dmp.digitaltwin.provider.consumer.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(value = {DigitaltwinMsgBaseChannel.class})
public class DigitaltwinMqBaseMessageConfig {
}
