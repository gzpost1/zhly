package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/6/14
 */
@Data
public class FormConfigDTO implements Serializable {

    private static final long serialVersionUID = -4868474492173821793L;

    /**
     * 表单名称
     */
    private String name;

    /**
     * 企业ID
     */
    private Long companyId;

}
