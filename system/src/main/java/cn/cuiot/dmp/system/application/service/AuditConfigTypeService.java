package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.AuditConfigTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.AuditConfigRspDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.AuditConfigTypeRspDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.system.application.param.dto.AuditConfigTypeDTO;
import cn.cuiot.dmp.system.application.param.dto.AuditConfigTypeQueryDTO;
import cn.cuiot.dmp.system.infrastructure.entity.AuditConfigEntity;
import cn.cuiot.dmp.system.infrastructure.entity.AuditConfigTypeEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.AuditConfigTypeMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/6/11
 */
@Slf4j
@Service
public class AuditConfigTypeService extends ServiceImpl<AuditConfigTypeMapper, AuditConfigTypeEntity> {

    @Autowired
    private AuditConfigService auditConfigService;

    /**
     * 查询详情
     */
    public AuditConfigTypeDTO queryForDetail(Long id) {
        AuditConfigTypeEntity auditConfigTypeEntity = Optional.ofNullable(getById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        AuditConfigTypeDTO auditConfigTypeDTO = new AuditConfigTypeDTO();
        BeanUtils.copyProperties(auditConfigTypeEntity, auditConfigTypeDTO);
        return auditConfigTypeDTO;
    }

    /**
     * 根据条件查询审核配置列表
     */
    public List<AuditConfigTypeRspDTO> queryForList(AuditConfigTypeReqDTO queryDTO) {
        List<AuditConfigEntity> auditConfigEntityList = auditConfigService.queryForList(queryDTO);
        if (CollectionUtils.isEmpty(auditConfigEntityList)) {
            return new ArrayList<>();
        }
        List<AuditConfigRspDTO> auditConfigRspDTOList = auditConfigEntityList.stream()
                .map(o -> {
                    AuditConfigRspDTO auditConfigRspDTO = new AuditConfigRspDTO();
                    BeanUtils.copyProperties(o, auditConfigRspDTO);
                    return auditConfigRspDTO;
                })
                .collect(Collectors.toList());
        Map<Byte, List<AuditConfigRspDTO>> auditConfigMap = auditConfigRspDTOList.stream()
                .collect(Collectors.groupingBy(AuditConfigRspDTO::getAuditConfigType));
        List<AuditConfigTypeRspDTO> auditConfigTypeDTOList = new ArrayList<>();
        auditConfigMap.forEach((key, value) -> {
            AuditConfigTypeDTO auditConfigTypeDTO = queryByType(key);
            AuditConfigTypeRspDTO auditConfigTypeRspDTO = new AuditConfigTypeRspDTO();
            BeanUtils.copyProperties(auditConfigTypeDTO, auditConfigTypeRspDTO);
            auditConfigTypeRspDTO.setAuditConfigList(value);
            auditConfigTypeDTOList.add(auditConfigTypeRspDTO);
        });
        return auditConfigTypeDTOList;
    }

    /**
     * 根据企业id查询审核配置列表
     */
    public List<AuditConfigTypeDTO> queryByCompany(Long companyId) {
        List<AuditConfigEntity> auditConfigEntityList = auditConfigService.queryByCompany(companyId);
        Map<Byte, List<AuditConfigEntity>> auditConfigMap = auditConfigEntityList.stream()
                .collect(Collectors.groupingBy(AuditConfigEntity::getAuditConfigType));
        List<AuditConfigTypeDTO> auditConfigTypeDTOList = new ArrayList<>();
        auditConfigMap.forEach((key, value) -> {
            AuditConfigTypeDTO auditConfigTypeDTO = queryByType(key);
            auditConfigTypeDTO.setAuditConfigList(value);
            auditConfigTypeDTOList.add(auditConfigTypeDTO);
        });
        return auditConfigTypeDTOList;
    }

    /**
     * 更新状态
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAuditConfigStatus(UpdateStatusParam updateStatusParam) {
        return auditConfigService.updateStatus(updateStatusParam);
    }

    private AuditConfigTypeDTO queryByType(Byte auditConfigType) {
        LambdaQueryWrapper<AuditConfigTypeEntity> queryWrapper = new LambdaQueryWrapper<AuditConfigTypeEntity>()
                .eq(AuditConfigTypeEntity::getAuditConfigType, auditConfigType);
        List<AuditConfigTypeEntity> auditConfigTypeEntities = list(queryWrapper);
        AssertUtil.isFalse(CollectionUtils.isEmpty(auditConfigTypeEntities), "审核配置类型不存在");
        AuditConfigTypeEntity auditConfigTypeEntity = auditConfigTypeEntities.get(0);
        AuditConfigTypeDTO auditConfigTypeDTO = new AuditConfigTypeDTO();
        BeanUtils.copyProperties(auditConfigTypeEntity, auditConfigTypeDTO);
        return auditConfigTypeDTO;
    }

}
