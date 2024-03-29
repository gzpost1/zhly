package cn.cuiot.dmp.system.infrastructure.entity.vo;

import lombok.Data;

/**
 * @author huw51
 */
@Data
public class CommercialComplexHouseDetailResVO extends BaseHouseDetailVo {

    /**
     * 商业综合体id
     */
    private Long commercialComplexId;

    /**
     * 商业综合体名称
     */
    private String commercialComplexName;

    /**
     * 商业综合体编号
     */
    private String commercialComplexCode;

    /**
     * 使用状态
     */
    private Integer usedStatus;
}
