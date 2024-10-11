package cn.cuiot.dmp.externalapi;

import cn.cuiot.dmp.base.infrastructure.dto.rsp.PlatfromInfoRespDTO;
import cn.cuiot.dmp.externalapi.provider.ExternalapiApplication;
import cn.cuiot.dmp.externalapi.service.sync.hik.HaikangAcsDoorDataSyncService;
import cn.cuiot.dmp.externalapi.service.sync.hik.HaikangPlatfromInfoCallable;
import cn.cuiot.dmp.externalapi.service.sync.hik.HaikangPlatfromInfoCallableService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author: wuyongchong
 * @date: 2024/10/11 11:47
 */
@Slf4j
@SpringBootTest(classes = ExternalapiApplication.class)
@RunWith(SpringRunner.class)
public class HaikangAcsDoorDataSyncTaskTest {

    @Autowired
    private HaikangAcsDoorDataSyncService haikangAcsDoorDataSyncService;

    @Autowired
    private HaikangPlatfromInfoCallableService haikangPlatfromInfoCallableService;

    @Test
    public void fullDataSyncTest() {
        haikangPlatfromInfoCallableService.resolvePlatfromInfos(1,
                new HaikangPlatfromInfoCallable() {
                    @Override
                    public void process(List<PlatfromInfoRespDTO> platfromInfoList) {
                        if (CollectionUtils.isNotEmpty(platfromInfoList)) {
                            Long companyId = platfromInfoList.get(0).getCompanyId();
                            haikangAcsDoorDataSyncService.haikangAcsDoorFullDataSync(companyId);
                        }
                    }
                });

    }

    @Test
    public void incrementDataSyncTest() {
        haikangPlatfromInfoCallableService.resolvePlatfromInfos(1,
                new HaikangPlatfromInfoCallable() {
                    @Override
                    public void process(List<PlatfromInfoRespDTO> platfromInfoList) {
                        if (CollectionUtils.isNotEmpty(platfromInfoList)) {
                            Long companyId = platfromInfoList.get(0).getCompanyId();
                            haikangAcsDoorDataSyncService.hikAcsDoorIncrementDataSync(companyId);
                        }
                    }
                });

    }

    @Test
    public void statusSyncTest() {
        haikangPlatfromInfoCallableService.resolvePlatfromInfos(1,
                new HaikangPlatfromInfoCallable() {
                    @Override
                    public void process(List<PlatfromInfoRespDTO> platfromInfoList) {
                        if (CollectionUtils.isNotEmpty(platfromInfoList)) {
                            Long companyId = platfromInfoList.get(0).getCompanyId();
                            haikangAcsDoorDataSyncService.hikAcsDoorStateSync(companyId);
                        }
                    }
                });
    }
}
