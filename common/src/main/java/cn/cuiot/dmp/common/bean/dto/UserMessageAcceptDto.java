package cn.cuiot.dmp.common.bean.dto;//	模板

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户消息消费接收参数
 *
 * @author hantingyao
 * @Description
 * @data 2024/5/27 10:36
 */
@Data
@Accessors(chain = true)
public class UserMessageAcceptDto {

    /**
     * 消息分类
     *
     * @see cn.cuiot.dmp.common.constant.MsgTypeConstant
     */
    private byte msgType;

    /**
     * 系统消息
     */
    private SysMsgDto sysMsgDto;

    /**
     * 短信消息
     */
    private SmsMsgDto smsMsgDto;
}
