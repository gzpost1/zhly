package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 查询门禁读卡器列表
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@Data
public class HikReaderReq extends HikBaseReq {

    /**
     * (非必填)
     * 名称，模糊搜索 若包含中文，最大长度指不超过按照指定编码的字节长度，即getBytes(“utf-8”).length 最大长度：32
     */
    private String name;

    /**
     * (非必填)
     * 是否包含下级区域，注： 1、参数isSubRegion和regionIndexCodes必须同时存在，且regionIndexCodes只能指定一个区域编码
     */
    private Boolean isSubRegion;

    /**
     * (非必填)
     * 设备能力集和后分析智能分析能力关系，默认为AND，AND：capabilitySet和intelligentSet都匹配 OR：capabilitySet或intelligentSet匹配
     */
    private String logicalRelationEnum;

    /**
     * (非必填)
     * 排序字段,注意：排序字段必须是查询条件，否则返回参数错误
     */
    private String orderBy;

    /**
     * (非必填)
     * 降序升序,降序：desc，升序：asc
     */
    private String orderType;

    /**
     * (非必填)
     * 开始日期,格式：yyyy-MM-dd'T'HH:mm:ss.SSS
     */
    private String startTime;
}
