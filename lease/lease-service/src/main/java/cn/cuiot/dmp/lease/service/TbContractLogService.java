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

import static cn.cuiot.dmp.common.constant.AuditContractConstant.*;
import static cn.cuiot.dmp.common.constant.AuditContractConstant.CONTRACT_INTENTION_TYPE;
import static cn.cuiot.dmp.common.constant.AuditContractConstant.CONTRACT_LEASE_TYPE;

/**
 * 合同操作日志 服务实现类
 *
 * @author Mujun
 * @since 2024-06-13
 */
@Service
public class TbContractLogService extends BaseMybatisServiceImpl<TbContractLogMapper, TbContractLogEntity> {


    /**
     * @param contractId
     * @param operate
     * @param contractType interntion 意向合同  lease租赁合同
     * @param operDesc
     * @param extId
     * @param path
     */
    public void saveLog(Long contractId, String operate, Integer contractType, String operDesc, String extId, String path) {
        String username = LoginInfoHolder.getCurrentLoginInfo().getName();
        TbContractLogEntity logEntity = new TbContractLogEntity();
        logEntity.setContractId(contractId);
        logEntity.setOperator(username);
        logEntity.setOperation(operate);
        logEntity.setOperDesc(username + " " + operDesc);
        logEntity.setExtId(extId);
        logEntity.setPath(path);
        logEntity.setOperationType(String.valueOf(contractType));
        logEntity.setOperTime(LocalDateTime.now());
        save(logEntity);
    }

    public void saveIntentionLog(Long contractId, String operate, String operDesc) {
        saveLog(contractId, operate, CONTRACT_INTENTION_TYPE, operDesc, null, null);
    }

    public void saveLeaseLog(Long contractId, String operate, String operDesc) {
        saveLog(contractId, operate, CONTRACT_LEASE_TYPE, operDesc, null, null);
    }
    public void saveLog(Long contractId, String operate, String operDesc,Integer contractType) {
        saveLog(contractId, operate, contractType, operDesc, null, null);
    }


    /**
     * 获取合同的操作日志
     */
    public void saveAuditLogMsg(Integer contractStatus, AuditParam auditParam,Integer contractType) {
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
            case STATUS_CHANGING:
                operate = OPERATE_CHANGE;
                break;
            case STATUS_RELETING:
                operate = OPERATE_LEASE_RELET;
                break;
            case STATUS_BACKING_LEASE:
                operate = OPERATE_LEASE_BACK;
                break;
            default:
                operate = ContractEnum.getEnumByCode(contractStatus).getDesc();
                break;
        }
        saveAuditLog(auditParam, contractType, operate);
    }

    public void saveAuditLog(AuditParam auditParam, Integer contractType, String operate) {
        String logMsg = getAuditMsg(auditParam, contractType, operate);
        String operation = operate + "审核";
        saveIntentionLog(auditParam.getId(), operation, logMsg);
    }



    public String getLogMsg(List<String> logMsgParam ,String temp){
        String logMsg = String.format(temp, logMsgParam.toArray());
        return logMsg;
    }

    public String getAuditMsg(AuditParam auditParam, Integer contractType, String operate) {
        List<String> logMsgParam = Lists.newLinkedList();
        String contractName = getContractTypeName(contractType);
        logMsgParam.add(contractName);
        logMsgParam.add(operate);
        if (Objects.equals(auditParam.getAuditStatus(), ContractEnum.AUDIT_PASS.getCode())) {
            logMsgParam.add(LOG_MSG_AUDIT_PASS);
        } else {
            logMsgParam.add(LOG_MSG_AUDIT_REFUSE);
        }
        logMsgParam.add(auditParam.getRemark());
        String logMsg = String.format(LOG_AUDIT_MSG_TEMP, logMsgParam.toArray());
        return logMsg;
    }

    public void saveOperateLog(Long contractId, String operate, Integer contractType){
        String operateMsg = getOperateMsg(contractType, operate);
        saveLog(contractId,operate,operateMsg,contractType);
    }


    public String getOperateMsg(Integer contractType, String operate){
        List<String> logMsgParam = Lists.newLinkedList();
        String contractName = getContractTypeName(contractType);
        logMsgParam.add(operate);
        logMsgParam.add(contractName);
        String logMsg = String.format(LOG_OPERATE_MSG_TEMP, logMsgParam.toArray());
        return logMsg;
    }



    public String getContractTypeName(Integer contractType) {
        String contractName = Objects.equals(contractType,CONTRACT_INTENTION_TYPE)? LOG_CONTRACT_INTENTION_NAME : LOG_CONTRACT_LEASE_NAME;
        return contractName;
    }

    public void removeByContractId(Long id) {
        LambdaQueryWrapper<TbContractLogEntity> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(TbContractLogEntity::getContractId, id);
        remove(queryWrapper);
    }

}
