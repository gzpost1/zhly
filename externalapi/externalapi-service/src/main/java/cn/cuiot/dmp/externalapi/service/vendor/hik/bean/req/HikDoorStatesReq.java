package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.req;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

/**
 * 门禁点状态req
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class HikDoorStatesReq {

    /**
     * (非必填)
     * 门禁点唯一标识，最大支持200个门禁点。[查询门禁点列表v2]@[软件产品-综合安防管理平台-API列表-一卡通应用服务-门禁管理#查询门禁点列表v2]接口获取返回参数doorIndexCode
     */
    private List<String> doorIndexCodes;
}
