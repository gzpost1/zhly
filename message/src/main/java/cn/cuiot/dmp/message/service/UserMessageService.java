package cn.cuiot.dmp.message.service;//	模板

import cn.cuiot.dmp.base.infrastructure.dto.req.MsgExistDataIdReqDto;
import cn.cuiot.dmp.message.dal.entity.UserMessageEntity;
import cn.cuiot.dmp.message.param.MessagePageQuery;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

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
     * @param ids
     */
    void readMessage(List<Long> ids);

    /**
     * 获取用户已经接收的消息数据id列表
     * @param reqDto
     * @return
     */
    List<Long> getAcceptDataIdList(MsgExistDataIdReqDto reqDto);

    /**
     * 获取未读消息数量
     * @return
     */
    Long getUnreadMessageCount();

    /**
     * 读取所有消息
     */
    void realAllMessage();
}
