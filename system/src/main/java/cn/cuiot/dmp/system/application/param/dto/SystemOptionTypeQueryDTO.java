package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/22
 */
@Data
public class SystemOptionTypeQueryDTO implements Serializable {

    private static final long serialVersionUID = -2604399464433734322L;

    /**
     * 系统选项类型名称
     */
    private String name;

    /**
     * 系统选项类型
     */
    private Byte systemOptionType;

    /**
     * 企业id
     */
    private Long companyId;

}
