package cn.cuiot.dmp.system.infrastructure.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Data;

/**
 * @author cwl
 * @classname ComplaintDetailsDto
 * @description 组织树vo
 * @date 2021/12/28
 */
@Data
public class DepartmentTreeVO {

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private Integer dGroup;

    private String departmentName;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long parentId;

    private Long pkOrgId;

    private Integer sort;

    private Integer level;

    private String path;

    private Integer isInit;

    /**
     * 注册时间
     */
    private LocalDateTime createdOn;

    /**
     * 创建者
     */
    private String createdBy;

    /**
     * 创建者
     */
    private String type;

    /**
     * 子集
     */
    private List<DepartmentTreeVO> children;

}
