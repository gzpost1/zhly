package cn.cuiot.dmp.externalapi.service.vo.hik;

import cn.cuiot.dmp.externalapi.service.entity.hik.HaikangAcsDoorEntity;
import lombok.Data;

/**
 * 门禁点信息
 * @author: wuyongchong
 * @date: 2024/10/10 14:20
 */
@Data
public class HaikangAcsDoorVo extends HaikangAcsDoorEntity {

    /**
     * 所属设备名称
     */
    private String parentIndexName;

    /**
     * 通道类型名称
     */
    private String channelTypeName;


    /**
     * 资源类型名称
     */
    private String resourceTypeName;

}
