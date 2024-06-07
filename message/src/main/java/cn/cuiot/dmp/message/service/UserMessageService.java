package cn.cuiot.dmp.message.service;//	模板

import cn.cuiot.dmp.message.dal.entity.UserMessageEntity;
import cn.cuiot.dmp.message.param.MessagePageQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/24 11:38
 */
public interface UserMessageService extends IService<UserMessageEntity> {

    /**
     * 查询用户消息
     * @param pageQuery
     * @return
     */
    IPage<UserMessageEntity> getMessage(MessagePageQuery pageQuery);

    /**
     * 标记消息已读
     * @param id
     */
    void readMessage(Long id);
}
