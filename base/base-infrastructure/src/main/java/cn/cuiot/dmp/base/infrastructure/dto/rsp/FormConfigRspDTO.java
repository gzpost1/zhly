package cn.cuiot.dmp.base.infrastructure.dto.rsp;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/9
 */
@Data
public class FormConfigRspDTO implements Serializable {

    private static final long serialVersionUID = -2807082267019162651L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 表单名称
     */
    private String name;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 分类ID
     */
    private Long typeId;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;

    /**
     * 表单配置详情
     */
    private String formConfigDetail;

}
