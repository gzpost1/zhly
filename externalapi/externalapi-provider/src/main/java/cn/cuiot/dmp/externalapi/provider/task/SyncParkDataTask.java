package cn.cuiot.dmp.externalapi.provider.task;

import cn.cuiot.dmp.externalapi.service.service.park.ParkInfoService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 同步停车场数据
 * @author pengjian
 * @create 2024/9/9 19:36
 */
@Slf4j
@Component
public class SyncParkDataTask {

    @Autowired
    private ParkInfoService parkInfoService;
    @XxlJob("syncParkData")
    public ReturnT<String> syncParkData(String param){
        log.info("同步停车场数据-车位数开始");
        parkInfoService.GetParkingLotList();
        log.info("同步停车场数据-车位数结束");
        return ReturnT.SUCCESS;
    }
}
