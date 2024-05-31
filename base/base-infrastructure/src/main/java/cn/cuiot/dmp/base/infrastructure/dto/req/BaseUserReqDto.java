package cn.cuiot.dmp.base.infrastructure.dto.req;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.List;

/**
 * 用户查询参数
 *
 * @author: wuyongchong
 * @date: 2024/5/10 10:08
 */
@Data
@Accessors(chain = true)
public class BaseUserReqDto implements Serializable {

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 角色ID列表
     */
    private List<Long> roleIdList;

    /**
     * 部门ID列表
     */
    private List<Long> deptIdList;

    /**
     * 用户ID列表
     */
    private List<Long> userIdList;

}
