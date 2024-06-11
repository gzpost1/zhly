package cn.cuiot.dmp.common.bean.dto;//	模板

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 用户消息消费接收参数
 * @author hantingyao
 * @Description
 * @data 2024/5/27 10:36
 */
@Data
public class UserMessageAcceptDto {

    /**
     * 消息分类
     * @see cn.cuiot.dmp.common.constant.MsgTypeConstant
     */
    private String msgType;

    /**
     * 发送人
     */
    private Long sendId;

    /**
     * 接受人
     */
    private List<Long> acceptors;

    /**
     * 数据ID
     */
    private Long dataId;

    /**
     * 消息
     */
    private String message;

    /**
     * 补充消息，可根据分类另行处理
     */
    private Object dataJson;

    /**
     * 消息发送时间
     */
    private Date messageTime;
}
