package cn.cuiot.dmp.externalapi.service.vendor.park.vo;

import cn.cuiot.dmp.externalapi.service.entity.park.VehicleExitRecordsEntity;
import lombok.Data;

import java.util.List;

/**
 * @author pengjian
 * @create 2024/9/9 17:50
 */
@Data
public class CarOutInfoVO {

    /**
     * 当前页
     */
    private Integer pageIndex;
    /**
     * 页数
     */
    private Integer pageSize;

    /**
     * 总数
     */
    private Integer totalCount;

    private List<VehicleExitRecordsEntity> detailList;
}
