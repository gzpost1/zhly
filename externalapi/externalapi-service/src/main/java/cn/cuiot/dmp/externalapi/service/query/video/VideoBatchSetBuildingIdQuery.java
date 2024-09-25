package cn.cuiot.dmp.externalapi.service.query.video;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 监控批量设置楼盘 query
 *
 * @Author: zc
 * @Date: 2024-09-04
 */
@Data
public class VideoBatchSetBuildingIdQuery {

    /**
     * 监控设备id列表
     */
    @NotEmpty(message = "监控设备不能为空")
    private List<Long> id;

    /**
     * 楼盘id
     */
    @NotNull(message = "楼盘不能为空")
    private Long buildingId;
}
