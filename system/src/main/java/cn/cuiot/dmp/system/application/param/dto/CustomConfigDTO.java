package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/22
 */
@Data
public class CustomConfigDTO implements Serializable {

    private static final long serialVersionUID = -6319866322692654652L;

    /**
     * 自定义配置名称
     */
    private String name;

    /**
     * 企业ID
     */
    private Long companyId;

}
