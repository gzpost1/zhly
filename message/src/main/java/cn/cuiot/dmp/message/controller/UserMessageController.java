package cn.cuiot.dmp.message.controller;//	模板

import cn.cuiot.dmp.base.application.annotation.ResolveExtData;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.message.dal.entity.UserMessageEntity;
import cn.cuiot.dmp.message.param.MessagePageQuery;
import cn.cuiot.dmp.message.param.MessagePageQueryReq;
import cn.cuiot.dmp.message.service.UserMessageService;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户消息
 *
 * @author hantingyao
 * @Description
 * @data 2024/6/7 16:26
 */

@RestController
@RequestMapping("/userMessage")
@ResolveExtData
public class UserMessageController {

    @Autowired
    private UserMessageService userMessageService;

    /**
     * 获取用户消息
     *
     * @param pageQuery
     * @return
     */
    @PostMapping("/getMessage")
    public IPage<UserMessageEntity> getMessage(@RequestBody MessagePageQuery pageQuery) {
        return userMessageService.getMessage(pageQuery);
    }

    /**
     * 读消息
     *
     * @param req
     * @return
     */
    @PostMapping("/readMessage")
    public Boolean readMessage(@RequestBody MessagePageQueryReq req) {
        if (CollUtil.isEmpty(req.getIds())) {
            return true;
        }
        userMessageService.readMessage(req.getIds());
        return true;
    }


    /**
     * 获取未读消息数量
     *
     * @return
     */
    @PostMapping("/getUnreadMessageCount")
    public Long getUnreadMessageCount() {
        return userMessageService.getUnreadMessageCount();
    }

    /**
     * 一键已读
     * @return
     */
    @PostMapping("/realAllMessage")
    public Boolean realAllMessage() {
        userMessageService.realAllMessage();
        return true;
    }
}
