package cn.cuiot.dmp.system.infrastructure.entity.vo;

import lombok.Data;

/**
 * 商务楼宇房屋详情dto
 * @author huw51
 */
@Data
public class CommercialBuildingHouseDetailResVO extends BaseHouseDetailVo {

    /**
     * 厂园区id
     */
    private Long parkId;

    /**
     * 厂园区名称
     */
    private String parkName;

    /**
     * 厂园区编号
     */
    private String parkCode;
}
