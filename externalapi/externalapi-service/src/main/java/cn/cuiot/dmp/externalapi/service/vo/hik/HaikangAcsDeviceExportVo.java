package cn.cuiot.dmp.externalapi.service.vo.hik;

import java.io.Serializable;
import lombok.Data;

/**
 * 门禁设备信息
 *
 * @author: wuyongchong
 * @date: 2024/10/10 14:20
 */
@Data
public class HaikangAcsDeviceExportVo implements Serializable {

    /**
     * 门禁点数量
     */
    private Integer doorCou;


    /**
     * 读卡器数量
     */
    private Integer readerCou;
}
