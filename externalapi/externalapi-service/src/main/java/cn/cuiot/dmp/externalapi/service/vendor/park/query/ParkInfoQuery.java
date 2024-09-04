package cn.cuiot.dmp.externalapi.service.vendor.park.query;

import cn.cuiot.dmp.common.bean.PageQuery;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
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
    private Integer parkId;

    /**
    * 总车位数
    */
    private Integer totalSpaceNum;

    /**
    * 楼盘id
    */
    private Long communityId;

    /**
    * 空闲车位数
    */
    private Integer freeSpaceNum;



}
