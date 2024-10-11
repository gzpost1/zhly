package cn.cuiot.dmp.externalapi.service.vo.hik;

import lombok.Data;

/**
 * 授权设备
 *
 * @Author: zc
 * @Date: 2024-10-10
 */
@Data
public class HikPersonAuthorizeVO {

    /**
     * 是否选中（0:否，1:是）
     */
    private Byte isSelect;

    /**
     * 第三方门禁id
     */
    private String thirdDoorId;

    /**
     * 所属区域目录名，符号 @ 分隔
     */
    private String regionPathName;

    /**
     * 资源名称
     */
    private String name;
}
