package cn.cuiot.dmp.system.infrastructure.persistence.mapper;

import lombok.Data;

/**
 * 获取员工的信息
 *
 * @author hk
 * @date 2023-11-1
 */
@Data
public class UserDepartmentEntity {
    /**
     * 用户id
     */
    private Long id;

    /**
     * 组织树
     */
    private String path;

    /**
     * 账户名称
     */
    private String orgName;
}
