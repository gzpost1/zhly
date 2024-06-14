package cn.cuiot.dmp.system.infrastructure.entity.dto;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

/**
 * @author: wuyongchong
 * @date: 2024/6/14 9:27
 */
@Data
public class ClientUserQuery extends PageQuery {
    /**
     * 昵称
     */
    private String name;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 企业ID-前端不用管
     */
    private Long companyId;
}
