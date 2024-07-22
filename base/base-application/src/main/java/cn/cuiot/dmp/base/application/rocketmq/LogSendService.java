package cn.cuiot.dmp.base.application.rocketmq;

import cn.cuiot.dmp.common.log.dto.OperateLogDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;

/**
 * @Description mq发送
 * @Date 2024/5/8 14:05
 * @Created by libo
 */
@Service
public class LogSendService {
    @Autowired
    private MsgChannel msgChannel;

    /**
     * 发送操作日志
     * @param operateLogDto
     */
    public void sendOperaLog(OperateLogDto operateLogDto) {
        // 记录日志
        msgChannel.operationlogOutput().send(messageBuilderObject(operateLogDto));
    }

    public <T> Message<T> messageBuilderObject(T data){
        return MessageBuilder.withPayload(data)
                .setHeader(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON)
                .build();
    }

}
