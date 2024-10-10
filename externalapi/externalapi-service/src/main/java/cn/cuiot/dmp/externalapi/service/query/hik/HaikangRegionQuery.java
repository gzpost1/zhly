package cn.cuiot.dmp.externalapi.service.query.hik;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

/**
 * 门禁设备查询参数
 *
 * @author: wuyongchong
 * @date: 2024/10/10 14:18
 */
@Data
public class HaikangRegionQuery extends PageQuery {
    /**
     * 企业ID-前端不用管
     */
    private Long companyId;

    /**
     * 资源类型（非必填）默认为region
     */
    private String resourceType;

    /**
     * 是否包含下级区域（非必填），true 时，搜索 parentIndexCodes 的所有子、孙区域；false 时，只搜索 parentIndexCodes 的直接子区域；默认为true
     */
    private Boolean isSubRegion;

    /**
     * 区域类型（非必填），10-普通区域，11-级联区域，12-楼栋单元
     */
    private Integer regionType;

    /**
     * 区域名称（非必填）
     */
    private String regionName;

    /**
     * 级联标识（非必填）， 0-全部， 1-本级， 2-级联， 默认值为 0
     */
    private Integer cascadeFlag;
}
