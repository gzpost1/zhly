package cn.cuiot.dmp.baseconsumer.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(value = {MsgBaseChannel.class})
public class MqBaseMessageConfig {
}
