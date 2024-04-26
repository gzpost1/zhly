package cn.cuiot.dmp.system.domain.aggregate;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/4/26
 */
@Data
public class CommonOption implements Serializable {

    private static final long serialVersionUID = -581125814590108701L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 常用选项名称
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

}
