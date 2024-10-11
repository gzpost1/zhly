package cn.cuiot.dmp.externalapi.provider.task.hik;

import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.externalapi.service.sync.hik.HaikangAcsReaderDataSyncService;
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
 * 海康门禁读卡器数据同步
 *
 * @author: wuyongchong
 * @date: 2024/10/11 9:29
 */
@Slf4j
@Component
public class HaikangAcsReaderDataSyncTask {

    @Autowired
    private HaikangAcsReaderDataSyncService haikangAcsReaderDataSyncService;

    @Autowired
    private HaikangPlatfromInfoCallableService haikangPlatfromInfoCallableService;

    /**
     * 门禁读卡器数据全量同步
     */
    @XxlJob("hikAcsReaderFullDataSyncJobHandler")
    public ReturnT<String> haikangAcsReaderFullDataSyncJobHandler(String param) {
        log.info("hikAcsReaderFullDataSyncJobHandler begin...");
        haikangPlatfromInfoCallableService.resolvePlatfromInfos(1,
                new HaikangPlatfromInfoCallable() {
                    @Override
                    public void process(List<PlatfromInfoRespDTO> platfromInfoList) {
                        if(CollectionUtils.isNotEmpty(platfromInfoList)){
                            Long companyId = platfromInfoList.get(0).getCompanyId();
                            haikangAcsReaderDataSyncService.haikangAcsReaderFullDataSync(companyId);
                        }
                    }
                });
        log.info("hikAcsReaderFullDataSyncJobHandler end...");
        return ReturnT.SUCCESS;
    }

    /**
     * 门禁读卡器数据增量同步
     */
    @XxlJob("hikAcsReaderIncrementDataSyncJobHandler")
    public ReturnT<String> hikAcsReaderIncrementDataSyncJobHandler(String param) {
        log.info("hikAcsReaderIncrementDataSyncJobHandler begin...");
        haikangPlatfromInfoCallableService.resolvePlatfromInfos(1,
                new HaikangPlatfromInfoCallable() {
                    @Override
                    public void process(List<PlatfromInfoRespDTO> platfromInfoList) {
                        if(CollectionUtils.isNotEmpty(platfromInfoList)){
                            Long companyId = platfromInfoList.get(0).getCompanyId();
                            haikangAcsReaderDataSyncService.hikAcsReaderIncrementDataSync(companyId);
                        }
                    }
                });
        log.info("hikAcsReaderIncrementDataSyncJobHandler end...");
        return ReturnT.SUCCESS;
    }

    /**
     * 门禁读卡器在线状态同步
     */
    @XxlJob("hikAcsReaderOnlineStatusSyncJobHandler")
    public ReturnT<String> hikAcsReaderOnlineStatusSyncJobHandler(String param) {
        log.info("hikAcsReaderOnlineStatusSyncJobHandler begin...");
        haikangPlatfromInfoCallableService.resolvePlatfromInfos(1,
                new HaikangPlatfromInfoCallable() {
                    @Override
                    public void process(List<PlatfromInfoRespDTO> platfromInfoList) {
                        if(CollectionUtils.isNotEmpty(platfromInfoList)){
                            Long companyId = platfromInfoList.get(0).getCompanyId();
                            haikangAcsReaderDataSyncService.hikAcsReaderOnlineStatusSync(companyId);
                        }
                    }
                });
        log.info("hikAcsReaderOnlineStatusSyncJobHandler end...");
        return ReturnT.SUCCESS;
    }

}
