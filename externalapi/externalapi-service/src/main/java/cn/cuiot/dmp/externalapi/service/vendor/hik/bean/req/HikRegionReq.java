package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询区域列表v2 req
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@Data
public class HikRegionReq extends HikBaseReq {

    /**
     * （必填）
     * 资源类型；详见数据字典： 附录A.3 资源类型/资源权限码； 查询资源的类型，传 region 时查询的为用户有配置权限的区域树，
     * 传资源类型如：camera、encodeDevice 查询用户对该类资源有权限的区域树；
     * 注：资源 iasDevice\reader\floor 不进行权限校验，即不传 authCodes
     */
    private String resourceType;

    /**
     * （非必填）
     * 是否包含下级区域，true 时，搜索 parentIndexCodes 的所有子、孙区域；
     * false 时，只搜索 parentIndexCodes 的直接子区域
     */
    private Boolean isSubRegion;

    /**
     * （非必填）
     * 区域类型，10-普通区域，11-级联区域，12-楼栋单元
     */
    private Integer regionType;

    /**
     * （非必填）
     * 区域名称；根据区域名称过滤，可模糊查询；
     * 若包含中文，最大长度 40，最大长度指不超过按照指定编码的字节长度，即 getBytes("utf-8").length
     */
    private String regionName;

    /**
     * （非必填）
     * 级联标识， 0-全部， 1-本级， 2-级联， 默认值为 0
     */
    private Integer cascadeFlag;

    /**
     * （非必填）
     * 排序字段，注意：排序字段必须是查询条件，否则返回参数错误
     */
    private String orderBy;

    /**
     * （非必填）
     * 排序方式，降序：desc，升序：asc
     */
    private String orderType;
}
