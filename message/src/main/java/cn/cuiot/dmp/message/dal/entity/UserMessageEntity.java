package cn.cuiot.dmp.message.dal.entity;

import cn.cuiot.dmp.base.infrastructure.persistence.handler.JsonTypeHandler;
import cn.cuiot.dmp.message.constant.StatusConstans;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/24 10:29
 */
@Data
@TableName(value = "tb_msg_user_message", autoResultMap = true)
public class UserMessageEntity {

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 消息分类
     */
    private String msgType;

    /**
     * 发送人
     */
    private Long sendId;

    /**
     * 接受人
     */
    private Long accepter;

    /**
     * 数据ID
     */
    private Long dataId;

    /**
     * 数据类型
     */
    private String dataType;

    /**
     * 消息
     */
    private String message;

    /**
     * 补充消息，可根据分类另行处理
     */
    @TableField(typeHandler = JsonTypeHandler.class)
    private Object dataJson;

    /**
     * 消息发送时间
     */
    private Date messageTime;

    /**
     * 是否已读
     */
    private Byte readStatus;

    /**
     * 读消息时间
     */
    private Date readTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 楼盘id(客户端必传)
     */
    private Long buildingId;

    /**
     * 用户类型（非空）
     * 见 UserTypeEnum
     */
    private Integer userType;

    public void init() {
        this.createTime = new Date();
        this.readStatus = StatusConstans.UNREAD;
    }
}
