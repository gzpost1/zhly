package cn.cuiot.dmp.archive.application.param.query;

import cn.cuiot.dmp.common.bean.PageQuery;
import java.util.List;
import lombok.Data;

/**
 * 管家查询条件
 * @author: wuyongchong
 * @date: 2024/6/7 14:03
 */
@Data
public class HouseKeeperQuery extends PageQuery {

    /**
     * 所属楼盘ID
     */
    private Long communityId;

    /**
     * 所属楼盘ID列表
     */
    private List<Long> communityIdList;

    /**
     * 状态(0:禁用,1:正常)
     */
    private Byte status;

    /**
     * 组织路径-前端不用管
     */
    private String deptPath;

    /**
     * 企业ID-前端不用管
     */
    private Long companyId;
}
