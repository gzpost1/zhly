package cn.cuiot.dmp.externalapi.provider.consumer.config;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.stereotype.Component;

/**
 * @Author: zc
 * @Date: 2024-09-24
 */
@Component
@EnableBinding(value = {SmsMsgBaseChannel.class})
public class SmsMqBaseMessageConfig {
}
