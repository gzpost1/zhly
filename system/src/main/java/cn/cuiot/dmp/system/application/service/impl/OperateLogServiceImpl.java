package cn.cuiot.dmp.system.application.service.impl;

import cn.cuiot.dmp.common.log.dto.OperateLogDto;
import cn.cuiot.dmp.base.application.service.OperateLogService;
import cn.cuiot.dmp.system.infrastructure.entity.OperationLogEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.dao.OperationLogDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * @Author 26432
 * @Date 2021/12/8   17:32
 */
@Slf4j
@Service
public class OperateLogServiceImpl implements OperateLogService {

    @Autowired
    private OperationLogDao operationLogDao;

    private static final int MAX_LENGTH = 2048;

    @Async
    @Override
    public Integer saveDb(OperateLogDto operateLogDTO) {
        //直接入库
        log.info("日志入库: {}", operateLogDTO);
        OperationLogEntity entity = new OperationLogEntity();
        BeanUtils.copyProperties(operateLogDTO, entity);
        // 防止消息体过长
        if (!StringUtils.isEmpty(entity.getRequestParams()) && entity.getRequestParams().length() > MAX_LENGTH) {
            entity.setRequestParams(entity.getRequestParams().substring(0, 2044) + "...");
        }
        if (!StringUtils.isEmpty(entity.getResponseParams()) && entity.getResponseParams().length() > MAX_LENGTH) {
            entity.setResponseParams(entity.getResponseParams().substring(0, 2044) + "...");
        }
        operationLogDao.insertOperationLog(entity);
        return 0;
    }
}
