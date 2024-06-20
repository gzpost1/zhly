package cn.cuiot.dmp.base.infrastructure.dto.req;

import cn.cuiot.dmp.common.enums.LabelManageTypeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/6/19
 */
@Data
public class LabelManageTypeReqDTO implements Serializable {

    private static final long serialVersionUID = -538217561943405419L;

    /**
     * 企业id
     */
    private Long companyId;

    /**
     * 标签管理类型
     * @see LabelManageTypeEnum
     */
    private Byte labelManageType;

}
