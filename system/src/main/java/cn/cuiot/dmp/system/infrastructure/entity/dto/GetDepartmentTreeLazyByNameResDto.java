package cn.cuiot.dmp.system.infrastructure.entity.dto;

import java.util.TreeSet;
import lombok.Data;

/**
 * @author wqd
 * @classname GetDepartmentTreeLazyByNameResDto
 * @description
 * @date 2022/6/7
 */
@Data
public class GetDepartmentTreeLazyByNameResDto implements Comparable<GetDepartmentTreeLazyByNameResDto>{

    private Long id;

    private String dGroup;

    private String departmentName;

    private Long parentId;

    private String path;

    private String level;

    private String hasChild;

    private TreeSet<GetDepartmentTreeLazyByNameResDto> childList;

    @Override
    public int compareTo(GetDepartmentTreeLazyByNameResDto o) {
        return this.id.compareTo(o.getId());
    }
}
