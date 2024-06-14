package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.entity.TbContractLogEntity;
import cn.cuiot.dmp.lease.mapper.TbContractLogMapper;
import cn.cuiot.dmp.base.application.mybatis.service.BaseMybatisServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 合同操作日志 服务实现类
 *
 * @author Mujun
 * @since 2024-06-13
 */
@Service
public class TbContractLogService extends BaseMybatisServiceImpl<TbContractLogMapper, TbContractLogEntity> {

    public void saveLog(String operation, String operDesc, String path) {
        String username = LoginInfoHolder.getCurrentLoginInfo().getUsername();
        TbContractLogEntity logEntity = new TbContractLogEntity();
        logEntity.setOperator(username);
        logEntity.setOperation(operation);
        logEntity.setOperDesc(username+":"+operDesc);
        logEntity.setPath(path);
        logEntity.setOperTime(LocalDateTime.now());
        save(logEntity);
    }

    public void saveLog(String operation, String operDesc) {
        saveLog(operation, operDesc, null);
    }
}
