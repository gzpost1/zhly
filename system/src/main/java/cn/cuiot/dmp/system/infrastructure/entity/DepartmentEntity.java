package cn.cuiot.dmp.system.infrastructure.entity;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @param
 * @Author xieSH
 * @Description 组织实体
 * @Date 2021/8/17 9:32
 * @return
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepartmentEntity {

    private Long id;

    private Integer dGroup;

    private String departmentName;

    private Long parentId;

    private Long pkOrgId;

    private int sort;

    private String code;

    private String path;

    private String description;

    private Integer isInit;

    private Integer level;

    /**
     * 注册时间
     */
    private LocalDateTime createdOn;

    /**
     * 创建者
     */
    private String createdBy;

    private String orgName;

    private String currentFloor;

    private String extend;

    private String regionType;

    private String otherRegionTypeName;
}
