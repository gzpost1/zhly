package cn.cuiot.dmp.app.config;//	模板

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * 系统管理消息配置
 * @author wuyongchong
 * @Description
 * @data 2024/5/27 10:11
 */
public interface SystemMsgChannel {

    String SYSTEM_INPUT = "systemInput";

    @Input(SYSTEM_INPUT)
    SubscribableChannel systemInput();

}