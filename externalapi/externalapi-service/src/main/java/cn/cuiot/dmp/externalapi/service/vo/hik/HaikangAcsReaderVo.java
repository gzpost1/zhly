package cn.cuiot.dmp.externalapi.service.vo.hik;

import cn.cuiot.dmp.externalapi.service.entity.hik.HaikangAcsReaderEntity;
import lombok.Data;

/**
 * 门禁读卡器信息
 *
 * @author: wuyongchong
 * @date: 2024/10/10 14:20
 */
@Data
public class HaikangAcsReaderVo extends HaikangAcsReaderEntity {

    /**
     * 所属设备名称
     */
    private String parentIndexName;


    /**
     * 所属门禁点名称
     */
    private String channelIndexName;

}
