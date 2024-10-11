package cn.cuiot.dmp.externalapi.service.sync.hik;

import cn.cuiot.dmp.externalapi.service.vendor.hik.HikApiFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 海康门禁设备数据同步服务
 * @author: wuyongchong
 * @date: 2024/10/11 9:48
 */
@Slf4j
@Component
public class HaikangAcsDeviceDataSyncService {

    @Autowired
    private HikApiFeignService hikApiFeignService;

    /**
     * 门禁设备数据全量同步
     */
    public void haikangAcsDeviceFullDataSync() {

    }

    /**
     * 门禁设备数据增量同步
     */
    public void hikAcsDeviceIncrementDataSync() {

    }

    /**
     * 门禁设备在线状态同步
     */
    public void hikAcsDeviceOnlineStatusSync() {

    }

}
