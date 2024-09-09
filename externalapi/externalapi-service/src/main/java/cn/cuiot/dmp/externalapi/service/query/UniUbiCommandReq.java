package cn.cuiot.dmp.externalapi.service.query;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 门禁（宇泛）操作命令下发请求
 *
 * @date 2024/8/21 14:13
 * @author gxp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UniUbiCommandReq implements Serializable {
    private static final long serialVersionUID = -8527541751522467489L;

    /**
     * 设备来源
     */
    private String source;

    /**
     * 设备序列号
     */
    private String deviceNo;

    /**
     * 操作类型 1 重启 2：重置 3：启用 4 禁用
     */
    private Integer operateType;

}
