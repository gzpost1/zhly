package cn.cuiot.dmp.message.dal.entity;

import cn.cuiot.dmp.message.constant.StatusConstans;
import com.baomidou.mybatisplus.annotation.IdType;
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
@TableName(value = "tb_msg_user_message")
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

    /**
     * 是否已读
     */
    private Byte readStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    public void init() {
        this.createTime = new Date();
        this.readStatus = StatusConstans.UNREAD;
    }
}
