package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/6/11
 */
@Data
public class AuditConfigTypeQueryDTO implements Serializable {

    private static final long serialVersionUID = 4629624699217321166L;

    /**
     * 企业id
     */
    private Long companyId;

}
