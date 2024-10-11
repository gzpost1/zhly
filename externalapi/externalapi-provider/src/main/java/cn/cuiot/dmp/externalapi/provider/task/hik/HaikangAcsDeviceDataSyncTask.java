package cn.cuiot.dmp.externalapi.provider.task.hik;

import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.externalapi.service.sync.hik.HaikangAcsDeviceDataSyncService;
import cn.cuiot.dmp.externalapi.service.sync.hik.HaikangPlatfromInfoCallable;
import cn.cuiot.dmp.externalapi.service.sync.hik.HaikangPlatfromInfoCallableService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
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

    @Autowired
    private HaikangPlatfromInfoCallableService haikangPlatfromInfoCallableService;

    /**
     * 门禁设备数据全量同步
     */
    @XxlJob("hikAcsDeviceFullDataSyncJobHandler")
    public ReturnT<String> haikangAcsDeviceFullDataSyncJobHandler(String param) {
        log.info("hikAcsDeviceFullDataSyncJobHandler begin...");
        haikangPlatfromInfoCallableService.resolvePlatfromInfos(1,
                new HaikangPlatfromInfoCallable() {
                    @Override
                    public void process(List<PlatfromInfoRespDTO> platfromInfoList) {
                        if(CollectionUtils.isNotEmpty(platfromInfoList)){
                            Long companyId = platfromInfoList.get(0).getCompanyId();
                            haikangAcsDeviceDataSyncService.haikangAcsDeviceFullDataSync(companyId);
                        }
                    }
                });
        log.info("hikAcsDeviceFullDataSyncJobHandler end...");
        return ReturnT.SUCCESS;
    }

    /**
     * 门禁设备数据增量同步
     */
    @XxlJob("hikAcsDeviceIncrementDataSyncJobHandler")
    public ReturnT<String> hikAcsDeviceIncrementDataSyncJobHandler(String param) {
        log.info("hikAcsDeviceIncrementDataSyncJobHandler begin...");
        haikangAcsDeviceDataSyncService.hikAcsDeviceIncrementDataSync();
        log.info("hikAcsDeviceIncrementDataSyncJobHandler end...");
        return ReturnT.SUCCESS;
    }

    /**
     * 门禁设备在线状态同步
     */
    @XxlJob("hikAcsDeviceOnlineStatusSyncJobHandler")
    public ReturnT<String> hikAcsDeviceOnlineStatusSyncJobHandler(String param) {
        log.info("hikAcsDeviceOnlineStatusSyncJobHandler begin...");
        haikangAcsDeviceDataSyncService.hikAcsDeviceOnlineStatusSync();
        log.info("hikAcsDeviceOnlineStatusSyncJobHandler end...");
        return ReturnT.SUCCESS;
    }

}
