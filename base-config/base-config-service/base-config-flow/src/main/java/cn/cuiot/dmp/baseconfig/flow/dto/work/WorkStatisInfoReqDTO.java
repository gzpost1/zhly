package cn.cuiot.dmp.baseconfig.flow.dto.work;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

import java.util.List;

@Data
public class WorkStatisInfoReqDTO extends PageQuery {


    /**
     * 企业id
     */
    private Long companyId;


    private List<Long> departmentIds;

    /**
     * 楼盘id列表
     */
    private List<Long> loupanIds;

    /**
     * 工单来源
     */
    private List<Byte> workSourceList;


    /**
     * 工单状态
     */
    private List<Byte> workStateList;

}
