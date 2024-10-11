package cn.cuiot.dmp.externalapi.provider.task.hik;

import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.externalapi.service.sync.hik.HaikangAcsDoorDataSyncService;
import cn.cuiot.dmp.externalapi.service.sync.hik.HaikangPlatfromInfoCallable;
import cn.cuiot.dmp.externalapi.service.sync.hik.HaikangPlatfromInfoCallableService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 海康门禁点数据同步
 *
 * @author: wuyongchong
 * @date: 2024/10/11 9:29
 */
@Slf4j
@Component
public class HaikangAcsDoorDataSyncTask {

    @Autowired
    private HaikangAcsDoorDataSyncService haikangAcsDoorDataSyncService;

    @Autowired
    private HaikangPlatfromInfoCallableService haikangPlatfromInfoCallableService;

    /**
     * 门禁点数据全量同步
     */
    @XxlJob("hikAcsDoorFullDataSyncJobHandler")
    public ReturnT<String> haikangAcsDoorFullDataSyncJobHandler(String param) {
        log.info("hikAcsDoorFullDataSyncJobHandler begin...");
        haikangPlatfromInfoCallableService.resolvePlatfromInfos(1,
                new HaikangPlatfromInfoCallable() {
                    @Override
                    public void process(List<PlatfromInfoRespDTO> platfromInfoList) {
                        if(CollectionUtils.isNotEmpty(platfromInfoList)){
                            Long companyId = platfromInfoList.get(0).getCompanyId();
                            haikangAcsDoorDataSyncService.haikangAcsDoorFullDataSync(companyId);
                        }
                    }
                });
        log.info("hikAcsDoorFullDataSyncJobHandler end...");
        return ReturnT.SUCCESS;
    }

    /**
     * 门禁点数据增量同步
     */
    @XxlJob("hikAcsDoorIncrementDataSyncJobHandler")
    public ReturnT<String> hikAcsDoorIncrementDataSyncJobHandler(String param) {
        log.info("hikAcsDoorIncrementDataSyncJobHandler begin...");
        haikangPlatfromInfoCallableService.resolvePlatfromInfos(1,
                new HaikangPlatfromInfoCallable() {
                    @Override
                    public void process(List<PlatfromInfoRespDTO> platfromInfoList) {
                        if(CollectionUtils.isNotEmpty(platfromInfoList)){
                            Long companyId = platfromInfoList.get(0).getCompanyId();
                            haikangAcsDoorDataSyncService.hikAcsDoorIncrementDataSync(companyId);
                        }
                    }
                });
        log.info("hikAcsDoorIncrementDataSyncJobHandler end...");
        return ReturnT.SUCCESS;
    }

    /**
     * 门禁点状态同步
     */
    @XxlJob("hikAcsDoorStateSyncJobHandler")
    public ReturnT<String> hikAcsDoorStateSyncJobHandler(String param) {
        log.info("hikAcsDoorStateSyncJobHandler begin...");
        haikangPlatfromInfoCallableService.resolvePlatfromInfos(1,
                new HaikangPlatfromInfoCallable() {
                    @Override
                    public void process(List<PlatfromInfoRespDTO> platfromInfoList) {
                        if(CollectionUtils.isNotEmpty(platfromInfoList)){
                            Long companyId = platfromInfoList.get(0).getCompanyId();
                            haikangAcsDoorDataSyncService.hikAcsDoorStateSync(companyId);
                        }
                    }
                });
        log.info("hikAcsDoorStateSyncJobHandler end...");
        return ReturnT.SUCCESS;
    }

}
