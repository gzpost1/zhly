package cn.cuiot.dmp.common.bean.dto;//	模板

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 用户业务消息消费接收参数（针对批量的不同用户不同消息场景）
 *
 * @Author: zc
 * @Date: 2024-06-26
 */
@Data
@Accessors(chain = true)
public class UserBusinessMessageAcceptDto {

    /**
     * 消息分类
     *
     * @see cn.cuiot.dmp.common.constant.MsgTypeConstant
     */
    private byte msgType;

    /**
     * 系统消息
     */
    private List<SysBusinessMsgDto> sysMsgDto;

    /**
     * 短信消息
     */
    private List<SmsBusinessMsgDto> smsMsgDto;
}