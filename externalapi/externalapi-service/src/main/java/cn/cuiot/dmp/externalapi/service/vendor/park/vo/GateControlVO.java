package cn.cuiot.dmp.externalapi.service.vendor.park.vo;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author pengjian
 * @create 2024/9/9 16:02
 */
@Data
public class GateControlVO {
    /**
     * 车场id
     */
    @NotNull(message = "车场id不能为空")
    private Integer parkId;

    /**
     * 通道id
     */
    @NotBlank(message = "通道id不能为空")
    private String nodeId;

    /**
     * 控制方式 0:道闸开(仅单次,车辆离开后,会自动关闭) 1:道闸关(从抬杠状态到关闭) 2:强制常开(道闸常开模式,车辆离开也不落杆) 3:结束常开(恢复正常模式,并且自动关闭)
     */
    @NotBlank(message = "控制方式不能为空")
    private String type;
}
