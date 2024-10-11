package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req;

import lombok.Data;

import java.util.List;

/**
 * 查询资源列表v2 req
 *
 * @Author: zc
 * @Date: 2024-10-11
 */
@Data
public class HikIrdsResourcesByParamsReq {

    /**
     * 名称，模糊搜索，最大长度32，若包含中文，最大长度指不超过按照指定编码的字节长度
     */
    private String name;

    /**
     * 区域编号,可以为空; 支持根据区域批量查询
     */
    private List<String> regionIndexCodes;

    /**
     * true时，搜索regionIndexCodes及其子孙区域的资源； false时，只搜索 regionIndexCodes的资源
     */
    private Boolean isSubRegion;

    /**
     * 资源类型，指设备资源，如门禁控制器、门禁点
     */
    private String resourceType;

    /**
     * 当前页码
     */
    private Long pageNo;

    /**
     * 分页大小
     */
    private Long pageSize;

    /**
     * 权限码集合
     */
    private List<String> authCodes;

    /**
     * 设备能力集(含设备上的智能能力)
     */
    private List<String> capabilitySet;

    /**
     * 排序字段,必须是查询条件
     */
    private String orderBy;

    /**
     * 排序类型，降序：desc 升序：asc
     */
    private String orderType;

    /**
     * 查询表达式集合
     */
    private List<Expression> expressions;

    /**
     * 查询表达式
     */
    @Data
    public static class Expression {

        /**
         * 资源属性名，如 updateTime
         */
        private String key;

        /**
         * 操作运算符，0：=，1：>=，2：<=，3：in，4：not in，5：between，6：like，7：pre like，8：suffix like
         */
        private Integer operator;

        /**
         * 资源属性值
         */
        private List<String> values;
    }
}
