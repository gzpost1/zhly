package cn.cuiot.dmp.externalapi.service.vo.hik;

import java.io.Serializable;
import lombok.Data;

/**
 * @author: wuyongchong
 * @date: 2024/10/18 11:15
 */
@Data
public class HikDoorControlVo implements Serializable {
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

    /**
     * 门禁点名称
     */
    private String name;
}
