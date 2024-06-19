package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.AuditConfigTypeReqDTO;
import cn.cuiot.dmp.common.constant.AuditConfigConstant;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.enums.AuditConfigTypeEnum;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.system.infrastructure.entity.AuditConfigEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.AuditConfigMapper;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author caorui
 * @date 2024/6/11
 */
@Slf4j
@Service
public class AuditConfigService extends ServiceImpl<AuditConfigMapper, AuditConfigEntity> {

    /**
     * 根据条件查询审核配置列表
     */
    public List<AuditConfigEntity> queryForList(AuditConfigTypeReqDTO queryDTO) {
        LambdaQueryWrapper<AuditConfigEntity> queryWrapper = new LambdaQueryWrapper<AuditConfigEntity>()
                .eq(Objects.nonNull(queryDTO.getCompanyId()), AuditConfigEntity::getCompanyId, queryDTO.getCompanyId())
                .eq(Objects.nonNull(queryDTO.getAuditConfigType()), AuditConfigEntity::getAuditConfigType, queryDTO.getAuditConfigType())
                .eq(StrUtil.isNotEmpty(queryDTO.getName()), AuditConfigEntity::getName, queryDTO.getName());
        return list(queryWrapper);
    }

    /**
     * 根据企业id查询审核配置列表
     */
    public List<AuditConfigEntity> queryByCompany(Long companyId) {
        LambdaQueryWrapper<AuditConfigEntity> queryWrapper = new LambdaQueryWrapper<AuditConfigEntity>()
                .eq(AuditConfigEntity::getCompanyId, companyId);
        List<AuditConfigEntity> auditConfigEntityList = list(queryWrapper);
        if (CollectionUtils.isNotEmpty(auditConfigEntityList)) {
            return auditConfigEntityList;
        }
        return initAuditConfig(companyId);
    }

    /**
     * 更新状态
     */
    public boolean updateStatus(UpdateStatusParam updateStatusParam) {
        AuditConfigEntity auditConfigEntity = Optional.ofNullable(getById(updateStatusParam.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        auditConfigEntity.setStatus(updateStatusParam.getStatus());
        return updateById(auditConfigEntity);
    }

    /**
     * 初始化审核配置
     */
    public List<AuditConfigEntity> initAuditConfig(Long companyId) {
        AssertUtil.notNull(companyId, "企业ID不能为空");
        LambdaQueryWrapper<AuditConfigEntity> queryWrapper = new LambdaQueryWrapper<AuditConfigEntity>()
                .eq(AuditConfigEntity::getCompanyId, companyId);
        List<AuditConfigEntity> auditConfigEntityCurrentList = list(queryWrapper);
        if (CollectionUtils.isNotEmpty(auditConfigEntityCurrentList)) {
            return auditConfigEntityCurrentList;
        }
        List<AuditConfigEntity> auditConfigEntityList = new ArrayList<>();
        // 公告管理
        AuditConfigConstant.NOTICE_MANAGE_INIT.forEach(o -> {
            AuditConfigEntity auditConfigEntity = new AuditConfigEntity();
            auditConfigEntity.setId(IdWorker.getId());
            auditConfigEntity.setCompanyId(companyId);
            auditConfigEntity.setName(o);
            auditConfigEntity.setAuditConfigType(AuditConfigTypeEnum.NOTICE_MANAGE.getCode());
            auditConfigEntity.setCreatedBy(AuditConfigConstant.DEFAULT_USER_ID.toString());
            auditConfigEntityList.add(auditConfigEntity);
        });
        // 图文管理
        AuditConfigConstant.CONTENT_MANAGE_INIT.forEach(o -> {
            AuditConfigEntity auditConfigEntity = new AuditConfigEntity();
            auditConfigEntity.setId(IdWorker.getId());
            auditConfigEntity.setCompanyId(companyId);
            auditConfigEntity.setName(o);
            auditConfigEntity.setAuditConfigType(AuditConfigTypeEnum.CONTENT_MANAGE.getCode());
            auditConfigEntity.setCreatedBy(AuditConfigConstant.DEFAULT_USER_ID.toString());
            auditConfigEntityList.add(auditConfigEntity);
        });
        // 定价管理
        AuditConfigConstant.PRICE_MANAGE_INIT.forEach(o -> {
            AuditConfigEntity auditConfigEntity = new AuditConfigEntity();
            auditConfigEntity.setId(IdWorker.getId());
            auditConfigEntity.setCompanyId(companyId);
            auditConfigEntity.setName(o);
            auditConfigEntity.setAuditConfigType(AuditConfigTypeEnum.PRICE_MANAGE.getCode());
            auditConfigEntity.setCreatedBy(AuditConfigConstant.DEFAULT_USER_ID.toString());
            auditConfigEntityList.add(auditConfigEntity);
        });
        // 租赁合同
        AuditConfigConstant.LEASE_CONTRACT_INIT.forEach(o -> {
            AuditConfigEntity auditConfigEntity = new AuditConfigEntity();
            auditConfigEntity.setId(IdWorker.getId());
            auditConfigEntity.setCompanyId(companyId);
            auditConfigEntity.setName(o);
            auditConfigEntity.setAuditConfigType(AuditConfigTypeEnum.LEASE_CONTRACT.getCode());
            auditConfigEntity.setCreatedBy(AuditConfigConstant.DEFAULT_USER_ID.toString());
            auditConfigEntityList.add(auditConfigEntity);
        });
        // 意向合同
        AuditConfigConstant.INTENTION_CONTRACT_INIT.forEach(o -> {
            AuditConfigEntity auditConfigEntity = new AuditConfigEntity();
            auditConfigEntity.setId(IdWorker.getId());
            auditConfigEntity.setCompanyId(companyId);
            auditConfigEntity.setName(o);
            auditConfigEntity.setAuditConfigType(AuditConfigTypeEnum.INTENTION_CONTRACT.getCode());
            auditConfigEntity.setCreatedBy(AuditConfigConstant.DEFAULT_USER_ID.toString());
            auditConfigEntityList.add(auditConfigEntity);
        });
        saveBatch(auditConfigEntityList);
        return auditConfigEntityList;
    }

}
