package cn.cuiot.dmp.message.config;//	模板

import org.springframework.cloud.stream.annotation.EnableBinding;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/27 19:14
 */
@EnableBinding(value = {MqMsgChannel.class})
public class MsgMqMessageConfig {

}
