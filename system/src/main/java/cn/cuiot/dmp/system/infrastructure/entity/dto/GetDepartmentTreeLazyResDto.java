package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wqd
 * @classname GetDepartmentTreeLazyResDto
 * @description
 * @date 2022/5/17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GetDepartmentTreeLazyResDto {

    private String id;

    private String dGroup;

    private String departmentName;

    private String parentId;

    private String path;

    private String level;

    private String hasChild;

}
