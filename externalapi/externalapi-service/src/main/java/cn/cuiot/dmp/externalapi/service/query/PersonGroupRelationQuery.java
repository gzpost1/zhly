package cn.cuiot.dmp.externalapi.service.query;

import lombok.Data;

import java.util.List;

/**
 * 人员分组关联表 query
 *
 * @Author: zc
 * @Date: 2024-09-05
 */
@Data
public class PersonGroupRelationQuery {

    /**
     * 业务类型
     */
    private Byte businessType;

    /**
     * 数据id列表
     */
    private Long dataId;

    /**
     * 数据id列表
     */
    private List<Long> dataIds;

    /**
     * 人员分组id
     */
    private Long personGroupId;
}
