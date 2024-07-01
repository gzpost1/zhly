package cn.cuiot.dmp.common.bean.dto;//	模板

import cn.cuiot.dmp.common.constant.MsgDataType;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 系统消息-业务消息
 *
 * @Author: zc
 * @Date: 2024-06-26
 */
@Data
@Accessors(chain = true)
public class SysBusinessMsgDto {

    /**
     * 发送人
     */
    private Long sendId;

    /**
     * 接收人（非空）
     */
    private Long acceptors;

    /**
     * 数据ID（非空）
     */
    private Long dataId;

    /**
     * 数据类型（非空）
     *
     * @see MsgDataType
     */
    private String dataType;

    /**
     * 消息（非空）
     */
    private String message;

    /**
     * 补充消息，可根据数据类型另行处理
     */
    private Object dataJson;

    /**
     * 消息发送时间（非空）
     */
    private Date messageTime;
}