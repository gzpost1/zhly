package cn.cuiot.dmp.externalapi.service.vendor.park.query;


import java.util.Date;
import java.io.Serializable;
import java.util.List;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Getter;
import lombok.Setter;
/**
 *
 * @author pengjian
 * @since 2024-09-09
 */
@Getter
@Setter
public class GateManagementQuery extends PageQuery {
    /**
    * 通道名称
    */
    private String nodeName;

    /**
    * 使用类别 0停车场内1入口2出口3中间入口4中间出口
    */
    private Integer useType;

    /**
    * 道闸状态1常抬0正常-1状态未知
    */
    private Integer status;

    /**
    * 企业id
    */
    private Long companyId;

    /**
    * 楼盘id
    */
    private List<Long> communityIds;

    /**
     * 车场id
     */
    private Integer parkId;



}
