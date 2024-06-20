package cn.cuiot.dmp.lease.task;

import cn.cuiot.dmp.lease.service.charge.TbChargePlainService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @Description 自动执行计费任务
 * @Date 2024/6/20 11:30
 * @Created by libo
 */
@Slf4j
@Component
public class ChargePlainTask {
    @Autowired
    private TbChargePlainService chargePlainService;

    /**
     * 每天生成计费任务
     * @param param
     * @return
     */
    @XxlJob("createChargePlainTaskBYDay")
    public ReturnT<String> createWork(String param) {


        return ReturnT.SUCCESS;
    }

}
