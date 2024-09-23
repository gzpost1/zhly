package cn.cuiot.dmp.externalapi.service.vo.watermeter;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author pengjian
 * @create 2024/9/6 15:25
 */
@Data
public class UpdateWaterManagementVO {
    /**
     * 设备的imei号
     */
    @NotNull(message = "imei号不能为空")
    private List<String> wsImeis;

    /**
     * 水表名称
     */
    private String waterName;

    /**
     * 楼盘id
     */
    private Long communityId;
}
