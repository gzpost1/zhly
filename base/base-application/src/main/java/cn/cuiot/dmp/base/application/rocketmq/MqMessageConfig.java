package cn.cuiot.dmp.base.application.rocketmq;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(value = {MsgChannel.class})
public class MqMessageConfig {
}
