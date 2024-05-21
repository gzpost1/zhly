package cn.cuiot.dmp.archive.domain.aggregate;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author caorui
 * @date 2024/5/20
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class BuildingArchivesPageQuery extends PageQuery {

    private static final long serialVersionUID = -5291447441996967650L;

    /**
     * 部门id
     */
    private Long departmentId;

    /**
     * 楼盘id
     */
    private Long id;

    /**
     * 楼盘名称
     */
    private String name;

}
