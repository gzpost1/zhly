package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 添加权限配置
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class HikAcpsAuthConfigAddReq {

    /**
     * 人员数据列表
     */
    private List<PersonDataDTO> personDatas;

    /**
     * 设备通道对象列表
     */
    private List<ResourceInfoDTO> resourceInfos;

    /**
     * 开始日期，采用ISO8601时间格式
     */
    private String startTime;

    /**
     * 结束日期，采用ISO8601时间格式
     */
    private String endTime;

    /**
     * 内部类：人员数据对象
     */
    @Data
    public static class PersonDataDTO {

        /**
         * 该参数和personDataType组合使用,
         * 当personDataType传入组织时,indexCodes传入的是组织ID,
         * 当personDataType传入人员时,indexCodes传入的是人员ID
         */
        private List<String> indexCodes;

        /**
         * 人员数据类型， person 人员， org 组织
         */
        private String personDataType;
    }

    /**
     * 内部类：设备通道对象
     */
    @Data
    public static class ResourceInfoDTO {

        /**
         * 资源的唯一标识
         */
        private String resourceIndexCode;

        /**
         * 资源类型
         */
        private String resourceType;

        /**
         * 通道号
         */
        private List<Number> channelNos;
    }
}
