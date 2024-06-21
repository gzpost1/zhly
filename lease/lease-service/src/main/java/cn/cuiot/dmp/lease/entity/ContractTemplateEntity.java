package cn.cuiot.dmp.lease.entity;

import cn.cuiot.dmp.base.infrastructure.persistence.handler.JsonTypeHandler;
import cn.cuiot.dmp.base.infrastructure.persistence.mapper.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 租赁管理-合同管理-合同模板
 *
 * @author caorui
 * @date 2024/6/21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@TableName(value = "contract_template", autoResultMap = true)
public class ContractTemplateEntity extends BaseEntity {

    private static final long serialVersionUID = -3874352279730757389L;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 合同模板名称
     */
    private String name;

    /**
     * 合同性质（系统配置自定义）
     */
    private Long natureId;

    /**
     * 合同类型（系统配置自定义）
     */
    private Long typeId;

    /**
     * 模板用途
     */
    private String usage;

    /**
     * 模板备注
     */
    private String remark;

    /**
     * 表单配置详情
     */
    private String formConfigDetail;

    /**
     * 收费项目列表（系统配置自定义）
     */
    @TableField(typeHandler = JsonTypeHandler.class)
    private List<String> chargeItemIds;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;

}
