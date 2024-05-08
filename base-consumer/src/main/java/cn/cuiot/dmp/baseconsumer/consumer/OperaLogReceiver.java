package cn.cuiot.dmp.baseconsumer.consumer;

import cn.cuiot.dmp.baseconsumer.config.MsgBaseChannel;
import cn.cuiot.dmp.baseconsumer.entity.OperateLogEntity;
import cn.cuiot.dmp.common.log.dto.OperateLogDto;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @Description 操作日志消费
 * @Date 2024/5/8 12:05
 * @Created by libo
 */
@Component
public class OperaLogReceiver {
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 接收操作日志保存至mongodb
     *
     * @param dto
     */
    @StreamListener(MsgBaseChannel.OPERATIONLOGINPUT)
    public void receiveInput3(@Payload OperateLogDto dto) {
        if (Objects.nonNull(dto)) {
            OperateLogEntity entity = new OperateLogEntity();
            BeanUtils.copyProperties(dto, entity);
            //保存至mongodb
            mongoTemplate.save(entity);
        }
    }
}
