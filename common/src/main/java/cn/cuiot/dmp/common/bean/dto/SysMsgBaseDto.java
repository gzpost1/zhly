package cn.cuiot.dmp.common.bean.dto;//	模板

import cn.cuiot.dmp.common.constant.MsgDataType;
import cn.cuiot.dmp.common.constant.MsgTypeConstant;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * @author hantingyao
 * @Description
 * @data 2024/7/1 10:26
 */
@Data
@Accessors(chain = true)
public class SysMsgBaseDto {

    /**
     * 发送人
     */
    private Long sendId;

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
     * 消息类型 (非空)
     *
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

    /**
     * 楼盘id(客户端必传)
     */
    private Long buildingId;

    /**
     * 用户类型（非空）
     * 见 UserTypeEnum
     */
    private Integer userType;
}
