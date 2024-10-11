package cn.cuiot.dmp.externalapi.provider.task.hik;

import cn.cuiot.dmp.externalapi.service.sync.hik.HaikangAcsDoorDataSyncService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * 门禁点数据全量同步
     */
    @XxlJob("hikAcsDoorFullDataSyncJobHandler")
    public ReturnT<String> haikangAcsDoorFullDataSyncJobHandler(String param) {

        return ReturnT.SUCCESS;
    }

    /**
     * 门禁点数据增量同步
     */
    @XxlJob("hikAcsDoorIncrementDataSyncJobHandler")
    public ReturnT<String> hikAcsDoorIncrementDataSyncJobHandler(String param) {

        return ReturnT.SUCCESS;
    }

    /**
     * 门禁点状态同步
     */
    @XxlJob("hikAcsDoorStateSyncJobHandler")
    public ReturnT<String> hikAcsDoorStateSyncJobHandler(String param) {

        return ReturnT.SUCCESS;
    }

}
