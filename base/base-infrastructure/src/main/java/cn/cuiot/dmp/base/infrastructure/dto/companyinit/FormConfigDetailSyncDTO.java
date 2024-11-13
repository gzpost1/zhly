package cn.cuiot.dmp.base.infrastructure.dto.companyinit;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/6
 */
@Data
public class FormConfigDetailSyncDTO implements Serializable {

    private static final long serialVersionUID = -40082029117159059L;

    /**
     * 表单配置ID
     */
    private Long id;

    /**
     * 表单配置详情
     */
    private String formConfigDetail;

}
