package cn.cuiot.dmp.externalapi.service.vo.gw.entranceguard;

import lombok.Data;

/**
 * 格物门禁-授权 VO
 *
 * @Author: zc
 * @Date: 2024-09-09
 */
@Data
public class GwEntranceGuardAuthorizeVO {

    /**
     * 门禁id
     */
    private Long entranceGuardId;

    /**
     * 门禁名称
     */
    private String entranceGuardName;

    /**
     * 授权 0-未授权；1-已授权
     */
    private Byte authorize;
}
