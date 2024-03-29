package cn.cuiot.dmp.system.infrastructure.entity.dto;

import lombok.Data;

/**
 * @author zjb
 * @classname IndicatorCardListResDto
 * @description 指标卡列表响应Dto
 * @date 2023/1/13
 */
@Data
public class IndicatorCardListPo {

    /**
     * 主键id
     */
    private String id;

    /**
     * 指标卡名称
     */
    private String name;

    /**
     * 对应数量
     */
    private Integer number;

}
