package cn.cuiot.dmp.content.param.req;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

import java.util.List;

@Data
public class NoticeStatisInfoReqVo extends PageQuery {

    /**
     * 区分是管理端，还是移动端
     */
    private Integer type;

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
