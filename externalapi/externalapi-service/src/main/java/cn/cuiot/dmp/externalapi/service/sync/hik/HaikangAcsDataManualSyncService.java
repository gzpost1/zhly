package cn.cuiot.dmp.externalapi.service.sync.hik;

import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author: wuyongchong
 * @date: 2024/10/14 9:44
 */
@Slf4j
@Component
public class HaikangAcsDataManualSyncService {

    @Autowired
    private HaikangAcsDeviceDataSyncService haikangAcsDeviceDataSyncService;

    @Autowired
    private HaikangAcsDoorDataSyncService haikangAcsDoorDataSyncService;

    @Autowired
    private HaikangAcsReaderDataSyncService haikangAcsReaderDataSyncService;

    @Autowired
    private HaikangPlatfromInfoCallableService haikangPlatfromInfoCallableService;


    /**
     * 门禁设备数据手动同步
     */
    public void haikangAcsDeviceDataManualSync(){
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
    }

    /**
     * 门禁点数据手动同步
     */
    public void haikangAcsDoorDataManualSync(){
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
    }

    /**
     * 门禁读卡器数据手动同步
     */
    public void haikangAcsReaderDataManualSync(){
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
    }

}
