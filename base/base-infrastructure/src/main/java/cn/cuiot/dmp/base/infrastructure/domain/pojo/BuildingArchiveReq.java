package cn.cuiot.dmp.base.infrastructure.domain.pojo;//	模板

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * @author hantingyao
 * @Description
 * @data 2024/5/30 16:56
 */
@Data
@Accessors(chain = true)
public class BuildingArchiveReq implements Serializable {

    /**
     * 部门id列表
     */
    private List<Long> departmentIdList;

    /**
     * 楼盘id列表
     */
    private List<Long> idList;
}
