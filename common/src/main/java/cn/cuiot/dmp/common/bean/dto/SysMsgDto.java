package cn.cuiot.dmp.common.bean.dto;//	模板

import cn.cuiot.dmp.common.constant.MsgDataType;
import cn.cuiot.dmp.common.constant.MsgTypeConstant;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/** 系统消息
 * @author hantingyao
 * @Description
 * @data 2024/6/14 10:26
 */
@Data
@Accessors(chain = true)
public class SysMsgDto {

    /**
     * 发送人
     */
    private Long sendId;

    /**
     * 接收人（非空）
     */
    private List<Long> acceptors;

    /**
     * 数据ID（非空）
     */
    private Long dataId;

    /**
     * 数据类型（非空）
     * @see MsgDataType
     */
    private String dataType;

    /**
     * 消息类型
     * @see MsgTypeConstant
     */
    private String msgType;

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
