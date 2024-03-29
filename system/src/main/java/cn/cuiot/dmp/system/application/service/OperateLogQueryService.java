package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.system.infrastructure.entity.bean.OperationLogBean;
import cn.cuiot.dmp.system.infrastructure.entity.dto.PlatformLogResDTO;

/**
 * 日志查询接口
 * @Author 26432
 * @Date 2021/12/8   17:37
 */
public interface OperateLogQueryService {

        /**
         * 获取操作日志列表
         *
         * @param operationLogBean
         * @return
         */
        PlatformLogResDTO listLogs(OperationLogBean operationLogBean);
}
