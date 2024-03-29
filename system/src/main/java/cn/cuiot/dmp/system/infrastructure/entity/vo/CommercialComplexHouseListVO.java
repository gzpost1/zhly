package cn.cuiot.dmp.system.infrastructure.entity.vo;

import lombok.Data;

/**
 * 商业综合体列表返回VO
 * @author huw51
 */
@Data
public class CommercialComplexHouseListVO extends BaseHouseListVo {

    /**
     * 商业综合体id
     */
    private Long commercialComplexId;

    /**
     * 商业综合体名称
     */
    private String commercialComplexName;


}
