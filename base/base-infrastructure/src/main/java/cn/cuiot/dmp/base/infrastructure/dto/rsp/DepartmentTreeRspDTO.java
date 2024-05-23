package cn.cuiot.dmp.base.infrastructure.dto.rsp;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author caorui
 * @date 2024/5/22
 */
@Data
public class DepartmentTreeRspDTO implements Serializable {

    private static final long serialVersionUID = -5568800907324710232L;

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
    private List<DepartmentTreeRspDTO> children;

}
