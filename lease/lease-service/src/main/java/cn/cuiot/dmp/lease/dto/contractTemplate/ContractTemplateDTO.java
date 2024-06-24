package cn.cuiot.dmp.lease.dto.contractTemplate;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author caorui
 * @date 2024/6/24
 */
@Data
public class ContractTemplateDTO implements Serializable {

    private static final long serialVersionUID = -9194568447682153973L;

    /**
     * 合同模板编码
     */
    private Long id;

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
     * 合同性质名称（系统配置自定义）
     */
    private String natureName;

    /**
     * 合同类型（系统配置自定义）
     */
    private Long typeId;

    /**
     * 合同类型名称（系统配置自定义）
     */
    private String typeName;

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
    private List<String> chargeItemIds;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdOn;

    /**
     * 创建者。取值：SYSTEM：系统生成的；{userKey}：Portal用户创建的；{appKey}：API创建的。
     */
    private String createdBy;

    /**
     * 创建者名称
     */
    private String createdName;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedOn;

    /**
     * 更新者。取值：{userKey}：Portal用户更新的；{appKey}：API更新的。
     */
    private String updatedBy;

    /**
     * 更新者名称
     */
    private String updatedName;

}
