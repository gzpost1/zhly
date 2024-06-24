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

    //意向合同
    public static final String TYPE_INTERNTION = "interntion";
    //租赁合同
    public static final String TYPE_LEASE = "lease";

    /**
     * @param contractId
     * @param operation
     * @param operationType interntion 意向合同  lease租赁合同
     * @param operDesc
     * @param extId
     * @param path
     */
    public void saveLog(Long contractId, String operation, String operationType, String operDesc, String extId, String path) {
        String username = LoginInfoHolder.getCurrentLoginInfo().getName();
        TbContractLogEntity logEntity = new TbContractLogEntity();
        logEntity.setContractId(contractId);
        logEntity.setOperator(username);
        logEntity.setOperation(operation);
        logEntity.setOperDesc(username + " " + operDesc);
        logEntity.setExtId(extId);
        logEntity.setPath(path);
        logEntity.setOperationType(operationType);
        logEntity.setOperTime(LocalDateTime.now());
        save(logEntity);
    }

    public void saveIntentionLog(Long contractId, String operation, String operDesc) {
        saveLog(contractId, operation, TYPE_INTERNTION, operDesc, null, null);
    }

    public void saveLeaseLog(Long contractId, String operation, String operDesc) {
        saveLog(contractId, operation, TYPE_LEASE, operDesc, null, null);
    }


    /**
     * 获取合同的操作日志
     */
    public void saveAuditLogMsg(Integer contractStatus, AuditParam auditParam) {
        List<String> logMsgParam = Lists.newLinkedList();
        ContractEnum contractEnum = ContractEnum.getEnumByCode(contractStatus);
        String operate = null;
        switch (contractEnum) {
            case STATUS_COMMITING:
                operate = OPERATE_COMMIT;
                break;
            case STATUS_SIGNING:
                operate = OPERATE_SIGN_CONTRACT;
                break;
            case STATUS_CANCELING:
                operate = OPERATE_CANCEL;
                break;
            case STATUS_USELESSING:
                operate = OPERATE_USELESS;
                break;
            default:
                operate = ContractEnum.getEnumByCode(contractStatus).getDesc();
                break;
        }
        logMsgParam.add(operate);
        if (Objects.equals(auditParam.getAuditStatus(), ContractEnum.AUDIT_PASS.getCode())) {
            logMsgParam.add(AUDIT_PASS);
        } else {
            logMsgParam.add(AUDIT_REFUSE);
        }
        logMsgParam.add(auditParam.getRemark());
        String logMsg = String.format(AUDIT_MSG_TEMP, logMsgParam.toArray());
        String operation = operate + "审核";
        saveIntentionLog(auditParam.getId(), operation, logMsg);
    }

    public void removeByContractId(Long id) {
        LambdaQueryWrapper<TbContractLogEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TbContractLogEntity::getContractId, id);
        remove(queryWrapper);
    }

}
