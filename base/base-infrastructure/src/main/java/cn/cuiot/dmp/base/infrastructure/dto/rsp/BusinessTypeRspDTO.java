package cn.cuiot.dmp.base.infrastructure.dto.rsp;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/6
 */
@Data
public class BusinessTypeRspDTO implements Serializable {

    private static final long serialVersionUID = -5793057841924431430L;

    /**
     * 业务类型主键ID
     */
    private Long businessTypeId;

    /**
     * 业务类型名称（层级结构）
     */
    private String treeName;

}
