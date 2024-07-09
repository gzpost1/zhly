package cn.cuiot.dmp.app.entity;

import cn.cuiot.dmp.base.infrastructure.persistence.handler.JsonTypeHandler;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * <p>
 * 用户反馈
 * </p>
 *
 * @author wuyongchong
 * @since 2024-06-14
 */
@Data
@TableName(value = "tb_user_feedback", autoResultMap = true)
public class UserFeedbackEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
    @TableId("id")
    private Long id;


    /**
     * 用户ID
     */
    private Long userId;


    /**
     * 昵称
     */
    private String name;


    /**
     * 手机号码
     */
    private String phone;


    /**
     * 企业ID
     */
    private Long companyId;


    /**
     * 组织ID
     */
    private Long deptId;


    /**
     * 组织名称
     */
    private String deptName;


    /**
     * 楼盘ID
     */
    private Long buildingId;


    /**
     * 楼盘名称
     */
    private String buildingName;


    /**
     * 反馈内容
     */
    private String feedbackContent;


    /**
     * 图片
     */
    @TableField(typeHandler = JsonTypeHandler.class)
    private List<String> images;


    /**
     * 状态(0:待回复,1:已回复)
     */
    private Byte status;


    /**
     * 回复时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date replyTime;


    /**
     * 回复用户ID
     */
    private Long replyUserId;


    /**
     * 回复人
     */
    private String replyUserName;

    /**
     * 回复内容
     */
    private String replyContent;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 创建人ID
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 更新人ID
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;


    /**
     * 头像
     */
    @TableField(exist = false)
    private String avatar;
}
