package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp;

import lombok.Data;

import java.util.List;

/**
 * 查询组织列表v2 resp
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@Data
public class HikOrgListResp {

    /**
     * 查询数据记录总数
     */
    private Integer total;

    /**
     * 当前页码
     */
    private Integer pageNo;

    /**
     * 每页记录总数
     */
    private Integer pageSize;

    /**
     * 组织列表
     */
    private List<DataItem> list;

    /**
     * 组织信息结构体
     */
    @Data
    public static class DataItem {

        /**
         * 组织唯一标识码
         */
        private String orgIndexCode;

        /**
         * 组织编号
         */
        private String organizationCode;

        /**
         * 组织名称，如默认部门
         */
        private String orgName;

        /**
         * 组织路径，格式为 "@组织唯一标识码@"
         */
        private String orgPath;

        /**
         * 父组织唯一标识码
         */
        private String parentOrgIndexCode;

        /**
         * 标识组织节点是否有权限操作
         */
        private Boolean available;

        /**
         * 标识组织节点是否为叶子节点
         */
        private Boolean leaf;

        /**
         * 显示顺序
         */
        private Integer sort;

        /**
         * 创建时间，遵循ISO8601标准
         */
        private String createTime;

        /**
         * 更新时间，遵循ISO8601标准
         */
        private String updateTime;
    }
}
