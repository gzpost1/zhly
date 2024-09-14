package cn.cuiot.dmp.externalapi.service.query;

import cn.cuiot.dmp.query.PageQuery;
import lombok.Data;

/**
 * 门禁（宇泛）设备 查询VO
 *
 * @date 2024/8/21 16:03
 * @author gxp
 */
@Data
public class UniUbiEntranceGuardQueryVO extends PageQuery {
    private static final long serialVersionUID = -7131014863370891399L;

    /**
     * 设备名称
     */
    private String name;

}
