package cn.cuiot.dmp.system.infrastructure.entity;

import lombok.Data;

/**
 * @author wqd
 * @classname UserPropertyEntity
 * @description
 * @date 2022/8/10
 */
@Data
public class UserPropertyEntity {

    public UserPropertyEntity(){

    }

    /**
     * 自增id
     */
    private Long id;

    /**
     * org_id
     */
    private Long orgId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 编码
     */
    private String propertyKey;

    /**
     * 值
     */
    private String val;

    public UserPropertyEntity(Long orgId,Long userId,String propertyKey,String val){
        this.orgId = orgId;
        this.userId = userId;
        this.propertyKey = propertyKey;
        this.val = val;
    }
}
