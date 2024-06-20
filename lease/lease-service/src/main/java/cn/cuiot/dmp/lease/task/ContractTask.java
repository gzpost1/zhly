package cn.cuiot.dmp.lease.task;//	模板

import cn.cuiot.dmp.lease.service.TbContractIntentionService;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ContractTask {

    @Autowired
    TbContractIntentionService contractIntentionService;

    /**
     * 定时检查意向合同状态
     *
     * @param param
     * @return
     */
    @XxlJob("")
    public ReturnT<String> publishNotice(String param) {
        log.info("--------------------开始更新意向合同状态-------------------");
        return ReturnT.SUCCESS;
    }

}
