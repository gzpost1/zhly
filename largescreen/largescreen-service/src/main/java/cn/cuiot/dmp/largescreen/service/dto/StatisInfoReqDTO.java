package cn.cuiot.dmp.largescreen.service.dto;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class StatisInfoReqDTO extends PageQuery {


    /**
     * 企业id
     */
    private Long companyId;


    private List<Long> departmentIdList;

    /**
     * 楼盘id列表
     */
    private List<Long> loupanIds;

}
