package cn.cuiot.dmp.system.infrastructure.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 门禁（宇泛）设备 操作VO
 *
 * @date 2024/8/21 14:13
 * @author gxp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class UniUbiEntranceGuardOperateVO implements Serializable {
    private static final long serialVersionUID = -6380526338175075171L;

    /**
     * 设备来源
     */
    @NotBlank(message = "设备来源不能为空")
    private String source;

    /**
     * 设备序列号
     */
    @NotBlank(message = "设备序列号不能为空")
    private String deviceNo;

    /**
     * 操作类型 1 重启 2：重置 3：启用 4 禁用
     */
    @NotNull(message = "操作类型不能为空")
    private Integer operateType;

}
