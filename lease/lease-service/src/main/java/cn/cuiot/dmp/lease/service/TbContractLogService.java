package cn.cuiot.dmp.lease.service;

import cn.cuiot.dmp.base.application.enums.ContractEnum;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.lease.dto.contract.AuditParam;
import cn.cuiot.dmp.lease.entity.TbContractLogEntity;
import cn.cuiot.dmp.lease.mapper.TbContractLogMapper;
import cn.cuiot.dmp.base.application.mybatis.service.BaseMybatisServiceImpl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static cn.cuiot.dmp.common.constant.AuditConstant.*;

/**
 * 合同操作日志 服务实现类
 *
 * @author Mujun
 * @since 2024-06-13
 */
@Service
public class TbContractLogService extends BaseMybatisServiceImpl<TbContractLogMapper, TbContractLogEntity> {


    public void saveLog(Long contractId,String operation, String operDesc, String extId, String path) {
        String username = LoginInfoHolder.getCurrentLoginInfo().getUsername();
        TbContractLogEntity logEntity = new TbContractLogEntity();
        logEntity.setContractId(contractId);
        logEntity.setOperator(username);
        logEntity.setOperation(operation);
        logEntity.setOperDesc(username + " " + operDesc);
        logEntity.setExtId(extId);
        logEntity.setPath(path);
        logEntity.setOperTime(LocalDateTime.now());
        save(logEntity);
    }
    public void saveLog(Long contractId,String operation, String operDesc) {
       saveLog(contractId,operation,operDesc,null,null);
    }


    /**
     * 获取合同的操作日志
     * 1.提交审核 2.签约审核 3.退订审核 4.作废审核
     */
    public void saveAuditLogMsg(AuditParam auditParam) {
        Integer auditType = auditParam.getAuditType();
        List<String> logMsgParam = Lists.newLinkedList();
        String operate = null;
        switch (auditType) {
            case 1:
                operate = OPERATE_COMMIT;
                break;
            case 2:
                operate = OPERATE_SIGN_CONTRACT;
                break;
            case 3:
                operate = OPERATE_CANCEL;
                break;
            case 4:
                operate = OPERATE_USELESS;
                break;
        }
        logMsgParam.add(operate);
        if (Objects.equals(auditParam.getAuditStatus(),ContractEnum.AUDIT_PASS.getCode())) {
            logMsgParam.add(AUDIT_PASS);
        } else {
            logMsgParam.add(AUDIT_REFUSE);
        }
        logMsgParam.add(auditParam.getRemark());
        String logMsg = String.format(AUDIT_MSG_TEMP, logMsgParam.toArray());
        String operation = operate + "审核";
        saveLog(auditParam.getId(), operation, logMsg);
    }

    public void removeByContractId(Long id){
        LambdaQueryWrapper<TbContractLogEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TbContractLogEntity::getContractId,id);
        remove(queryWrapper);
    }

}
