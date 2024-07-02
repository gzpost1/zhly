package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.req.LabelManageTypeReqDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.LabelManageRspDTO;
import cn.cuiot.dmp.base.infrastructure.dto.rsp.LabelManageTypeRspDTO;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.system.application.param.dto.LabelManageTypeDTO;
import cn.cuiot.dmp.system.infrastructure.entity.LabelManageEntity;
import cn.cuiot.dmp.system.infrastructure.entity.LabelManageTypeEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.LabelManageTypeMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/6/19
 */
@Slf4j
@Service
public class LabelManageTypeService extends ServiceImpl<LabelManageTypeMapper, LabelManageTypeEntity> {

    @Autowired
    private LabelManageService labelManageService;

    /**
     * 查询详情
     */
    public LabelManageTypeDTO queryForDetail(Long id) {
        LabelManageTypeEntity labelManageTypeEntity = Optional.ofNullable(getById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        LabelManageTypeDTO labelManageTypeDTO = new LabelManageTypeDTO();
        BeanUtils.copyProperties(labelManageTypeEntity, labelManageTypeDTO);
        return labelManageTypeDTO;
    }

    /**
     * 根据条件查询标签管理列表
     */
    public List<LabelManageTypeRspDTO> queryForList(LabelManageTypeReqDTO queryDTO) {
        List<LabelManageEntity> labelManageEntityList = labelManageService.queryForList(queryDTO);
        if (CollectionUtils.isEmpty(labelManageEntityList)) {
            return new ArrayList<>();
        }
        List<LabelManageRspDTO> labelManageRspDTOList = labelManageEntityList.stream()
                .map(o -> {
                    LabelManageRspDTO labelManageRspDTO = new LabelManageRspDTO();
                    BeanUtils.copyProperties(o, labelManageRspDTO);
                    return labelManageRspDTO;
                })
                .collect(Collectors.toList());
        Map<Byte, List<LabelManageRspDTO>> LabelManageMap = labelManageRspDTOList.stream()
                .collect(Collectors.groupingBy(LabelManageRspDTO::getLabelManageType));
        List<LabelManageTypeRspDTO> labelManageTypeDTOList = new ArrayList<>();
        LabelManageMap.forEach((key, value) -> {
            LabelManageTypeDTO labelManageTypeDTO = queryByType(key);
            LabelManageTypeRspDTO labelManageTypeRspDTO = new LabelManageTypeRspDTO();
            BeanUtils.copyProperties(labelManageTypeDTO, labelManageTypeRspDTO);
            labelManageTypeRspDTO.setLabelManageList(value);
            labelManageTypeDTOList.add(labelManageTypeRspDTO);
        });
        return labelManageTypeDTOList;
    }

    /**
     * 根据企业id查询标签管理列表
     */
    public List<LabelManageTypeDTO> queryByCompany(Long companyId) {
        List<LabelManageEntity> labelManageEntityList = labelManageService.queryByCompany(companyId);
        Map<Byte, List<LabelManageEntity>> labelManageMap = labelManageEntityList.stream()
                .collect(Collectors.groupingBy(LabelManageEntity::getLabelManageType));
        List<LabelManageTypeDTO> LabelManageTypeDTOList = new ArrayList<>();
        labelManageMap.forEach((key, value) -> {
            LabelManageTypeDTO labelManageTypeDTO = queryByType(key);
            labelManageTypeDTO.setLabelManageList(value);
            LabelManageTypeDTOList.add(labelManageTypeDTO);
        });
        return LabelManageTypeDTOList;
    }

    private LabelManageTypeDTO queryByType(Byte labelManageType) {
        LambdaQueryWrapper<LabelManageTypeEntity> queryWrapper = new LambdaQueryWrapper<LabelManageTypeEntity>()
                .eq(LabelManageTypeEntity::getLabelManageType, labelManageType);
        List<LabelManageTypeEntity> labelManageTypeEntities = list(queryWrapper);
        AssertUtil.isFalse(CollectionUtils.isEmpty(labelManageTypeEntities), "标签管理类型不存在");
        LabelManageTypeEntity labelManageTypeEntity = labelManageTypeEntities.get(0);
        LabelManageTypeDTO labelManageTypeDTO = new LabelManageTypeDTO();
        BeanUtils.copyProperties(labelManageTypeEntity, labelManageTypeDTO);
        return labelManageTypeDTO;
    }
    
}
