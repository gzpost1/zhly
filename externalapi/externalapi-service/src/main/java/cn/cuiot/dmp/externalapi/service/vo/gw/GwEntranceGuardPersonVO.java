package cn.cuiot.dmp.externalapi.service.vo.gw;

import lombok.Data;

/**
 * 人员管理分页
 *
 * @Author: zc
 * @Date: 2024-09-09
 */
@Data
public class GwEntranceGuardPersonVO {

    /**
     * 人员名称
     */
    private String name;

    /**
     * 分组id
     */
    private Long personGroupId;

    /**
     * 授权 0-未授权；1-已授权
     */
    private Byte authorize;
}
