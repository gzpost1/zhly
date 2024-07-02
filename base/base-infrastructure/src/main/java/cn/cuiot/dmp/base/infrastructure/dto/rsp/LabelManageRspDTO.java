package cn.cuiot.dmp.base.infrastructure.dto.rsp;

import cn.cuiot.dmp.common.enums.LabelManageTypeEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author caorui
 * @date 2024/6/19
 */
@Data
public class LabelManageRspDTO implements Serializable {

    private static final long serialVersionUID = -4639481613495667007L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 企业ID
     */
    private Long companyId;

    /**
     * 标签管理类型
     * @see LabelManageTypeEnum
     */
    private Byte labelManageType;

    /**
     * 标签名称
     */
    private String labelName;

}
