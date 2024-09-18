package cn.cuiot.dmp.externalapi.service.vo.watermeter;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 物联网水表（山东科德）上报数据 查询VO
 *
 * @date 2024/8/21 14:13
 * @author gxp
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class WaterMeterQueryVO extends PageQuery {
    private static final long serialVersionUID = -2296079048473860663L;

    /**
     * 设备的imei号
     */
    private String wsImei;

    /**
     * 楼盘id
     */
    private List<Long> communityIds;

    /**
     * 水表名称
     */
    private String waterName;

    /**
     * 阀门状态
     */
    private String valveStatus;

    /**
     * 楼盘信息为空
     */
    private String communityIdType;
}
