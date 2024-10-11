package cn.cuiot.dmp.externalapi.provider.task.hik;

import cn.cuiot.dmp.externalapi.service.sync.hik.HaikangAcsReaderDataSyncService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
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

    /**
     * 门禁读卡器数据全量同步
     */
    @XxlJob("hikAcsReaderFullDataSyncJobHandler")
    public ReturnT<String> haikangAcsReaderFullDataSyncJobHandler(String param) {

        return ReturnT.SUCCESS;
    }

    /**
     * 门禁读卡器数据增量同步
     */
    @XxlJob("hikAcsReaderIncrementDataSyncJobHandler")
    public ReturnT<String> hikAcsReaderIncrementDataSyncJobHandler(String param) {

        return ReturnT.SUCCESS;
    }

    /**
     * 门禁读卡器在线状态同步
     */
    @XxlJob("hikAcsReaderOnlineStatusSyncJobHandler")
    public ReturnT<String> hikAcsReaderOnlineStatusSyncJobHandler(String param) {

        return ReturnT.SUCCESS;
    }

}
