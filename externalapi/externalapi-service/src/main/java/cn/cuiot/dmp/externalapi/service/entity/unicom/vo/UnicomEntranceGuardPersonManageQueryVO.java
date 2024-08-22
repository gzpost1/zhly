package cn.cuiot.dmp.externalapi.service.entity.unicom.vo;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

/**
 * 联通格物门禁-人员管理(UnicomEntranceGuardPersonManageEntity)查询VO
 *
 * @author Gxp
 * @since 2024-08-22 15:47:11
 */
@Data
public class UnicomEntranceGuardPersonManageQueryVO extends PageQuery {
    private static final long serialVersionUID = 4722668947551232456L;

    /**
     * 人员姓名
     */
    private String name;

}
