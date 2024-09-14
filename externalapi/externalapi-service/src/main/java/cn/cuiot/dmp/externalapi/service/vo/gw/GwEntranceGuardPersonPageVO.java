package cn.cuiot.dmp.externalapi.service.vo.gw;

import cn.cuiot.dmp.externalapi.service.entity.gw.GwEntranceGuardPersonEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 格物门禁-人员分组信息 VO
 *
 * @Author: zc
 * @Date: 2024-09-10
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class GwEntranceGuardPersonPageVO extends GwEntranceGuardPersonEntity {

    /**
     * 楼盘
     */
    private String buildingName;

    /**
     * 所属组织
     */
    private String deptPathName;

    /**
     * 分组信息
     */
    private String personGroupName;
}
