package cn.cuiot.dmp.externalapi.service.query.video;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

import java.util.List;

@Data
public class VideoStatisInfoReqDTO extends PageQuery {


    /**
     * 企业id
     */
    private Long companyId;


    private List<Long> departmentIdList;

    /**
     * 楼盘id列表
     */
    private List<Long> loupanIds;


    /**
     * 设备状态
     * 1: 未注册
     * 2: 在线
     * 3: 离线
     */
    private Integer state;

}
