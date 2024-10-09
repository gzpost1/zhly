package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 删除权限配置 req
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class HikAcpsAuthConfigDeleteReq {

    /**
     * 人员数据列表
     */
    private List<PersonDataDTO> personDatas;

    /**
     * 人员数据类型， person 人员， org 组织
     */
    private String personDataType;

    /**
     * 设备通道对象列表
     */
    private List<ResourceInfoDTO> resourceInfos;

    /**
     * 内部类：人员数据对象
     */
    @Data
    public static class PersonDataDTO {

        /**
         * 人员数据编号
         */
        private List<String> indexCodes;
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
        private List<String> channelNos;
    }
}
