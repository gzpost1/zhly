package cn.cuiot.dmp.archive.application.param.query;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

/**
 * 客户查询参数
 * @author: wuyongchong
 * @date: 2024/6/12 11:18
 */
@Data
public class CustomerQuery extends PageQuery {





    /**
     * 企业ID-前端不用管
     */
    private Long companyId;
}
