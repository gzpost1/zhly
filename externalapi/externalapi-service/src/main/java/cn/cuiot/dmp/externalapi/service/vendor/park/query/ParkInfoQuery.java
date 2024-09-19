package cn.cuiot.dmp.externalapi.service.vendor.park.query;

import cn.cuiot.dmp.common.bean.PageQuery;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 *
 * @author pengjian
 * @since 2024-09-03
 */
@Getter
@Setter

public class ParkInfoQuery extends PageQuery {
    /**
    * 车场名称
    */
    private String parkName;

    /**
    * 车场id
    */
    private String parkId;

    /**
    * 总车位数
    */
    private Integer totalSpaceNum;

    /**
    * 楼盘id
    */
    private List<Long> communityIds;

    /**
    * 空闲车位数
    */
    private Integer freeSpaceNum;

    /**
     * 企业id
     */
    private Long companyId;

}
