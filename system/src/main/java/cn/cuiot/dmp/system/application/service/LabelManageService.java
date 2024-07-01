package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.req.LabelManageTypeReqDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.param.dto.LabelManageCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.LabelManageUpdateDTO;
import cn.cuiot.dmp.system.infrastructure.entity.LabelManageEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.LabelManageMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author caorui
 * @date 2024/6/19
 */
@Slf4j
@Service
public class LabelManageService extends ServiceImpl<LabelManageMapper, LabelManageEntity> {

    /**
     * 根据条件查询线索管理列表
     */
    public List<LabelManageEntity> queryForList(LabelManageTypeReqDTO queryDTO) {
        LambdaQueryWrapper<LabelManageEntity> queryWrapper = new LambdaQueryWrapper<LabelManageEntity>()
                .eq(Objects.nonNull(queryDTO.getCompanyId()), LabelManageEntity::getCompanyId, queryDTO.getCompanyId())
                .eq(Objects.nonNull(queryDTO.getLabelManageType()), LabelManageEntity::getLabelManageType, queryDTO.getLabelManageType());
        return list(queryWrapper);
    }

    /**
     * 根据企业id查询线索管理列表
     */
    public List<LabelManageEntity> queryByCompany(Long companyId) {
        LambdaQueryWrapper<LabelManageEntity> queryWrapper = new LambdaQueryWrapper<LabelManageEntity>()
                .eq(LabelManageEntity::getCompanyId, companyId);
        return list(queryWrapper);
    }

    /**
     * 新增
     */
    public boolean saveLabelManage(LabelManageCreateDTO createDTO) {
        Long companyId = LoginInfoHolder.getCurrentOrgId();
        AssertUtil.notNull(companyId, "企业id不能为空");
        createDTO.setCompanyId(companyId);
        LabelManageEntity labelManageEntity = new LabelManageEntity();
        BeanUtils.copyProperties(createDTO, labelManageEntity);
        return save(labelManageEntity);
    }

    /**
     * 更新
     */
    public boolean updateLabelManage(LabelManageUpdateDTO updateDTO) {
        LabelManageEntity labelManageEntity = Optional.ofNullable(getById(updateDTO.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        labelManageEntity.setLabelList(updateDTO.getLabelList());
        return updateById(labelManageEntity);
    }

}
