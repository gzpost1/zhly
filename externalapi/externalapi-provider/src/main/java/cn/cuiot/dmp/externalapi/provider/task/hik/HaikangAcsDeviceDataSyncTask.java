package cn.cuiot.dmp.externalapi.provider.task.hik;

import cn.cuiot.dmp.externalapi.service.sync.hik.HaikangAcsDeviceDataSyncService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 海康门禁设备数据同步
 *
 * @author: wuyongchong
 * @date: 2024/10/11 9:29
 */
@Slf4j
@Component
public class HaikangAcsDeviceDataSyncTask {

    @Autowired
    private HaikangAcsDeviceDataSyncService haikangAcsDeviceDataSyncService;

    /**
     * 门禁设备数据全量同步
     */
    @XxlJob("hikAcsDeviceFullDataSyncJobHandler")
    public ReturnT<String> haikangAcsDeviceFullDataSyncJobHandler(String param) {

        return ReturnT.SUCCESS;
    }

    /**
     * 门禁设备数据增量同步
     */
    @XxlJob("hikAcsDeviceIncrementDataSyncJobHandler")
    public ReturnT<String> hikAcsDeviceIncrementDataSyncJobHandler(String param) {

        return ReturnT.SUCCESS;
    }

    /**
     * 门禁设备在线状态同步
     */
    @XxlJob("hikAcsDeviceOnlineStatusSyncJobHandler")
    public ReturnT<String> hikAcsDeviceOnlineStatusSyncJobHandler(String param) {

        return ReturnT.SUCCESS;
    }

}
