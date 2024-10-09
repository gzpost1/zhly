package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 门禁状态req
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@EqualsAndHashCode(callSuper = true)
@Data
public class HikAcsStatesReq extends HikBaseReq {

    /**
     * (非必填)
     * 区域唯一标志，根据[查询区域列表v2]@[软件产品-综合安防管理平台-API列表-资源目录-区域信息接口#查询区域列表v2]接口获取返回报文中的indexCode字段
     */
    private String regionId;

    /**
     * (非必填)
     * 设备ip
     */
    private String ip;

    /**
     * (非必填)
     * 编码列表，最大500
     */
    private List<String> indexCodes;

    /**
     * (非必填)
     * 状态，1-在线，0-离线，-1-未检测
     */
    private String status;

    /**
     * (非必填)
     * 是否包含下级，1包含，0不包含（若regionId为空，则该参数不起作用）
     */
    private String includeSubNode;
}
