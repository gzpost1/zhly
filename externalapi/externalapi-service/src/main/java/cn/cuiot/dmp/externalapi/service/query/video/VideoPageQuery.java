package cn.cuiot.dmp.externalapi.service.query.video;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 监控后台-分页vo
 *
 * @Author: zc
 * @Date: 2024-08-22
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class VideoPageQuery extends PageQuery {

    /**
     * 设备名称
     */
    private String deviceName;

    /**
     * 楼盘id
     */
    private Long buildingId;

    /**
     * 设备状态
     * 1: 未注册
     * 2: 在线
     * 3: 离线
     */
    private Integer state;
}
