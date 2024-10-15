package cn.cuiot.dmp.baseconfig.flow.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class StatisInfoReqDto implements Serializable {


    /**
     * 企业id
     */
    private Long companyId;

    /**
     * 组织部门
     */
    private List<Long> departmentIds;

    /**
     * 楼盘id列表
     */
    private List<Long> loupanIds;

}
