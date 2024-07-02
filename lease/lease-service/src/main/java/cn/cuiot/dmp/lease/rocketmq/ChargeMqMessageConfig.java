package cn.cuiot.dmp.lease.rocketmq;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.stereotype.Component;

@Component
@EnableBinding(value = {ChargeMsgChannel.class})
public class ChargeMqMessageConfig {
}