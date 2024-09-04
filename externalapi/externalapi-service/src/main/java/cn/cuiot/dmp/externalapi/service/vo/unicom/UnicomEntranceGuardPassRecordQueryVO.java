package cn.cuiot.dmp.externalapi.service.vo.unicom;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

/**
 * 联通格物门禁-通行记录(UnicomEntranceGuardPassRecordEntity)查询VO
 *
 * @author Gxp
 * @since 2024-08-22 16:04:56
 */
@Data
public class UnicomEntranceGuardPassRecordQueryVO extends PageQuery {
    private static final long serialVersionUID = 4931484877400093724L;

    /**
     * 人员名称
     */
    private String personName;

}
