package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 资源列表req
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@Data
public class HikDoorReq extends HikBaseReq {

    /**
     * (非必填)
     * 名称，模糊搜索，最大长度32，若包含中文，最大长度指不超过按照指定编码的字节长度，即getBytes("utf-8").length
     */
    private String name;

    /**
     * (非必填)
     * true时，搜索regionIndexCodes及其子孙区域的资源； false时，只搜索 regionIndexCodes的资源
     */
    private Boolean isSubRegion;

    /**
     * (非必填)
     * 排序字段,注意：排序字段必须是查询条件，否则返回参数错误
     */
    private String orderBy;

    /**
     * (非必填)
     * 降序升序,降序：desc 升序：asc
     */
    private String orderType;
}
