package cn.cuiot.dmp.externalapi.service.query;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

import java.util.List;

/**
 * @author pengjian
 * @create 2024/9/4 16:55
 */
@Data
public class QueryAccessCommunity extends PageQuery {

    /**
     * 设备序列号
     */
    private String deviceNo;

    /**
     * 名字
     */
    private String name;

    /**
     * 设备状态,1:未绑定 2:已绑定 3:已禁用
     */
    private String state;

    /**
     * 设备在线状态, 1:在线 2：不在线
     */
    private String onlineState;

    /**
     * 楼盘id
     */
    private List<Long> communityIds;

    /**
     * 楼盘查询信息
     */
    private String communityIdType;

    /**
     * 企业id
     */
    private Long companyId;
}
