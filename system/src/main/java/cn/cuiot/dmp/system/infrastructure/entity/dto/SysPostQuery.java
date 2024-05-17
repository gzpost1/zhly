package cn.cuiot.dmp.system.infrastructure.entity.dto;

import cn.cuiot.dmp.common.bean.PageQuery;
import lombok.Data;

/**
 * 岗位查询参数
 *
 * @author: wuyongchong
 * @date: 2024/5/6 10:03
 */
@Data
public class SysPostQuery extends PageQuery {

    /**
     * 岗位名称
     */
    private String postName;

    /**
     * 状态 0-禁用 1-启用
     */
    private Byte status;

    /**
     * 当前登录用户-账户id(前端不用管)
     */
    private Long sessionOrgId;

}
