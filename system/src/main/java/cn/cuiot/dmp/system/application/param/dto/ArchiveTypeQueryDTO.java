package cn.cuiot.dmp.system.application.param.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/22
 */
@Data
public class ArchiveTypeQueryDTO implements Serializable {

    private static final long serialVersionUID = -2604399464433734322L;

    /**
     * 档案类型名称
     */
    private String name;

    /**
     * 档案类型
     */
    private Byte archiveType;

    /**
     * 企业id
     */
    private Long companyId;

    /**
     * 用户id
     */
    private String userId;

}
