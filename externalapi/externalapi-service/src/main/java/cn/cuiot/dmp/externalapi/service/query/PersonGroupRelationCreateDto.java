package cn.cuiot.dmp.externalapi.service.query;

import lombok.Data;

/**
 * 人员分组关联表 dto
 *
 * @Author: zc
 * @Date: 2024-09-05
 */
@Data
public class PersonGroupRelationCreateDto {

    /**
     * 业务类型
     */
    private Byte businessType;

    /**
     * 数据id列表
     */
    private Long dataId;

    /**
     * 人员分组id
     */
    private Long personGroupId;
}
