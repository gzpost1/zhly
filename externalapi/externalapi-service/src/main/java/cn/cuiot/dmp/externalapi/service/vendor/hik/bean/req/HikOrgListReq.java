package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 查询组织列表v2
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@Data
public class HikOrgListReq extends HikBaseReq {

    /**
     * 组织名称，如默认部门
     */
    private String orgName;

    /**
     * 组织唯一标识码集合，多个值使用英文逗号分隔，不超过1000个
     */
    private String orgIndexCodes;

    /**
     * 父组织唯一标识码集合，多个值使用英文逗号分隔，不超过1000个
     */
    private String parentOrgIndexCodes;

    /**
     * 是否搜索所有子孙组织，true 搜索所有子孙组织，false 搜索直接子组织
     */
    private Boolean isSubOrg;

    /**
     * 查询表达式集合
     */
    private List<Expression> expressions;

    /**
     * 排序字段，必须是查询条件
     */
    private String orderBy;

    /**
     * 排序类型，降序：desc，升序：asc
     */
    private String orderType;

    /**
     * 查询表达式内部类
     */
    @Data
    public static class Expression {

        /**
         * 资源属性名，传入 updateTime 可查询特定时间段更新的数据
         */
        private String key;

        /**
         * 操作运算符，0：=，1：>=，2：<=，3：in，4：not in，5：between，6：like，7：pre like，8：suffix like
         */
        private Integer operator;

        /**
         * 资源属性值数组，支持的操作符不同，数组长度要求不同
         */
        private List<String> values;
    }
}
