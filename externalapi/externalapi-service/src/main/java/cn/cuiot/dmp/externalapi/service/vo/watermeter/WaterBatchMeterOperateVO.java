package cn.cuiot.dmp.externalapi.service.vo.watermeter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.List;

/**
 * 物联网水表（山东科德）下发阀控指令 操作VO
 *
 * @date 2024/8/21 14:13
 * @author gxp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class WaterBatchMeterOperateVO implements Serializable {
    private static final long serialVersionUID = -7983134084717524230L;

    /**
     * 设备的imei号
     */
    @NotBlank(message = "设备的imei号不能为空")
    private List<String> imei;

    /**
     * 对设备执行的阀门操作 00开阀 01关阀
     */
    @NotBlank(message = "对设备执行的阀门操作不能为空")
    private String valveControlType;

}
