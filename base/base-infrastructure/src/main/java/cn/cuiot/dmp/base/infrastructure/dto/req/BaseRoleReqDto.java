package cn.cuiot.dmp.base.infrastructure.dto.req;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * 角色查询参数
 * @author: wuyongchong
 * @date: 2024/5/10 10:08
 */
@Data
public class BaseRoleReqDto implements Serializable {

    /**
     * 企业ID
     */
    private Long orgId;

    /**
     * 角色ID列表
     */
    private List<Long> roleIdList;

}
