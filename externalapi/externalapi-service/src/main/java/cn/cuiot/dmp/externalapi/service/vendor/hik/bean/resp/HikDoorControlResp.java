package cn.cuiot.dmp.externalapi.service.vendor.hik.bean.resp;

import lombok.Data;

/**
 * 门禁点反控 resp
 *
 * @Author: zc
 * @Date: 2024-10-09
 */
@Data
public class HikDoorControlResp {

    /**
     * 门禁点唯一标识
     */
    private String doorIndexCode;

    /**
     * 0标识反控成功，其他表示失败，见附录E.3 门禁管理错误码说明
     */
    private Integer controlResultCode;

    /**
     * 与controlResultCode对应的描述，见附录E.3 门禁管理错误码说明
     */
    private String controlResultDesc;
}
