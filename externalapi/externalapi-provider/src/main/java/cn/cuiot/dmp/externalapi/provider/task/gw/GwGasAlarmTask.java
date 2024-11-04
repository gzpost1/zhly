package cn.cuiot.dmp.externalapi.provider.task.gw;

import cn.cuiot.dmp.externalapi.service.service.gw.gasalarm.GwGasAlarmTaskHandle;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 格物燃气报警器定时任务
 *
 * @Author: zc
 * @Date: 2024-10-24
 */
@Slf4j
@Component
public class GwGasAlarmTask {

    @Autowired
    private GwGasAlarmTaskHandle taskHandle;

    /**
     * 同步设备属性
     */
    @XxlJob("syncGwGasAlarmProperty")
    public ReturnT<String> syncGwGasAlarmProperty(String param) {
        log.info("开始同步格物燃气报警器属性.............");
        taskHandle.syncGwGasAlarmPropertyHandle(param);
        log.info("结束同步格物燃气报警器属性.............");
        return ReturnT.SUCCESS;
    }

    /**
     * 同步设备信息
     */
    @XxlJob("syncGwGasAlarmInfo")
    public ReturnT<String> syncGwGasAlarmInfo(String param) {
        log.info("开始同步格物燃气报警器信息.............");
        taskHandle.syncGwGasAlarmInfoHandle(param);
        log.info("结束同步格物燃气报警器信息.............");
        return ReturnT.SUCCESS;
    }
}
