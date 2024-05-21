package cn.cuiot.dmp.system.application.param.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/20
 */
@Data
public class ArchiveTypeVO implements Serializable {

    private static final long serialVersionUID = -7575184364510905465L;

    /**
     * 主键ID
     */
    private Long id;

    /**
     * 档案类型名称
     */
    private String name;

    /**
     * 档案类型
     */
    private Byte archiveType;

}
