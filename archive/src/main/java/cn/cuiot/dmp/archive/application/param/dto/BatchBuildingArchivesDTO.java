package cn.cuiot.dmp.archive.application.param.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author caorui
 * @date 2024/5/20
 */
@Data
public class BatchBuildingArchivesDTO implements Serializable {

    private static final long serialVersionUID = -8991567854676620220L;

    /**
     * 所属部门
     */
    private Long departmentId;

}
