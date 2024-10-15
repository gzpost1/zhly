package cn.cuiot.dmp.largescreen.service.dto.external;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

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
