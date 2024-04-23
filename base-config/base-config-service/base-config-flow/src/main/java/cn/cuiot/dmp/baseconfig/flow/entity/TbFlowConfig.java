package cn.cuiot.dmp.baseconfig.flow.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 系统配置-流程配置
 */
@Data
@TableName(value = "tb_flow_config")
public class TbFlowConfig {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.INPUT)
    private Long id;

    /**
     * 流程名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 业务分类
     */
    @TableField(value = "business_type_id")
    private Long businessTypeId;

    /**
     * 所属组织
     */
    @TableField(value = "org_id")
    private Long orgId;

    /**
     * 流程配置
     */
    @TableField(value = "`process`")
    private String process;

    /**
     * logo
     */
    @TableField(value = "logo")
    private String logo;

    /**
     * 消息通知设置
     */
    @TableField(value = "notify_setting")
    private String notifySetting;

    /**
     * 流程说明
     */
    @TableField(value = "remark")
    private String remark;

    /**
     * 通用配置
     */
    @TableField(value = "common_config_dto")
    private String commonConfigDto;

    /**
     * 状态 0停用 1启用
     */
    @TableField(value = "`status`")
    private Byte status;

    /**
     * 企业ID
     */
    @TableField(value = "company_id")
    private Long companyId;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 更新人
     */
    @TableField(value = "update_user")
    private Long updateUser;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 创建人
     */
    @TableField(value = "create_user")
    private Long createUser;

    /**
     * 是否删除 0否 1是
     */
    @TableField(value = "deleted")
    private Byte deleted;
}