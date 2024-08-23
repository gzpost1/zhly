package cn.cuiot.dmp.externalapi.service.entity.video.query;

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
     * 企业ID（前端不用传）
     */
    private Long companyId;
}
