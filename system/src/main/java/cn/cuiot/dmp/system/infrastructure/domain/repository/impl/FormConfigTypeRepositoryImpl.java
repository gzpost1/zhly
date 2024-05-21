package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.utils.TreeUtil;
import cn.cuiot.dmp.system.application.constant.FormConfigConstant;
import cn.cuiot.dmp.system.application.param.vo.FormConfigTypeTreeNodeVO;
import cn.cuiot.dmp.system.domain.aggregate.FormConfig;
import cn.cuiot.dmp.system.domain.aggregate.FormConfigPageQuery;
import cn.cuiot.dmp.system.domain.aggregate.FormConfigType;
import cn.cuiot.dmp.system.domain.repository.FormConfigRepository;
import cn.cuiot.dmp.system.domain.repository.FormConfigTypeRepository;
import cn.cuiot.dmp.system.infrastructure.entity.FormConfigTypeEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.FormConfigTypeMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/4/26
 */
@Slf4j
@Repository
public class FormConfigTypeRepositoryImpl implements FormConfigTypeRepository {

    @Autowired
    private FormConfigTypeMapper formConfigTypeMapper;

    @Autowired
    private FormConfigRepository formConfigRepository;

    @Override
    public FormConfigType queryForDetail(Long id) {
        FormConfigTypeEntity formConfigTypeEntity = Optional.ofNullable(formConfigTypeMapper.selectById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        FormConfigType formConfigType = new FormConfigType();
        BeanUtils.copyProperties(formConfigTypeEntity, formConfigType);
        return formConfigType;
    }

    @Override
    public List<FormConfigType> queryForList(List<Long> idList) {
        LambdaQueryWrapper<FormConfigTypeEntity> queryWrapper = new LambdaQueryWrapper<FormConfigTypeEntity>()
                .in(FormConfigTypeEntity::getId, idList);
        List<FormConfigTypeEntity> formConfigTypeEntityList = formConfigTypeMapper.selectList(queryWrapper);
        return formConfigTypeEntityList.stream()
                .map(o -> {
                    FormConfigType formConfigType = new FormConfigType();
                    BeanUtils.copyProperties(o, formConfigType);
                    return formConfigType;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<FormConfigType> queryByCompany(Long companyId) {
        LambdaQueryWrapper<FormConfigTypeEntity> queryWrapper = new LambdaQueryWrapper<FormConfigTypeEntity>()
                .eq(FormConfigTypeEntity::getCompanyId, companyId);
        List<FormConfigTypeEntity> formConfigTypeEntityList = formConfigTypeMapper.selectList(queryWrapper);
        List<FormConfigType> formConfigTypeList = new ArrayList<>();
        // 如果该企业下没有数据，默认创建一条"全部"作为根节点的数据
        if (CollectionUtils.isEmpty(formConfigTypeEntityList)) {
            FormConfigTypeEntity formConfigTypeEntity = initRootNode(companyId);
            FormConfigType formConfigType = new FormConfigType();
            BeanUtils.copyProperties(formConfigTypeEntity, formConfigType);
            formConfigTypeList.add(formConfigType);
            return formConfigTypeList;
        }
        formConfigTypeList = formConfigTypeEntityList.stream()
                .map(o -> {
                    FormConfigType formConfigType = new FormConfigType();
                    BeanUtils.copyProperties(o, formConfigType);
                    return formConfigType;
                })
                .collect(Collectors.toList());
        return formConfigTypeList;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveFormConfigType(FormConfigType formConfigType) {
        checkFormConfigTypeNode(formConfigType);
        FormConfigTypeEntity formConfigTypeEntity = new FormConfigTypeEntity();
        BeanUtils.copyProperties(formConfigType, formConfigTypeEntity);
        formConfigTypeEntity.setPathName(fillPathName(formConfigType));
        return formConfigTypeMapper.insert(formConfigTypeEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateFormConfigType(FormConfigType formConfigType) {
        checkFormConfigTypeNode(formConfigType);
        FormConfigTypeEntity formConfigTypeEntity = Optional.ofNullable(formConfigTypeMapper.selectById(formConfigType.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BeanUtils.copyProperties(formConfigType, formConfigTypeEntity);
        formConfigTypeEntity.setPathName(fillPathName(formConfigType));
        return formConfigTypeMapper.updateById(formConfigTypeEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteFormConfigType(List<String> idList) {
        return formConfigTypeMapper.deleteBatchIds(idList);
    }

    @Override
    public PageResult<FormConfig> queryFormConfigByType(FormConfigPageQuery pageQuery) {
        return formConfigRepository.queryFormConfigByType(pageQuery);
    }

    @Override
    public Long getRootTypeId(Long companyId) {
        AssertUtil.notNull(companyId, "企业id不能为空");
        LambdaQueryWrapper<FormConfigTypeEntity> queryWrapper = new LambdaQueryWrapper<FormConfigTypeEntity>()
                .eq(FormConfigTypeEntity::getCompanyId, companyId)
                .eq(FormConfigTypeEntity::getParentId, FormConfigConstant.DEFAULT_PARENT_ID);
        List<FormConfigTypeEntity> formConfigTypeEntityList = formConfigTypeMapper.selectList(queryWrapper);
        AssertUtil.notEmpty(formConfigTypeEntityList, "该企业下不存在表单配置分类");
        return formConfigTypeEntityList.get(0).getId();
    }

    private FormConfigTypeEntity initRootNode(Long companyId) {
        FormConfigTypeEntity formConfigTypeEntity = new FormConfigTypeEntity();
        formConfigTypeEntity.setId(IdWorker.getId());
        formConfigTypeEntity.setCompanyId(companyId);
        formConfigTypeEntity.setName(FormConfigConstant.ROOT_NAME);
        formConfigTypeEntity.setLevelType(FormConfigConstant.ROOT_LEVEL_TYPE);
        formConfigTypeEntity.setParentId(FormConfigConstant.DEFAULT_PARENT_ID);
        formConfigTypeEntity.setPathName(FormConfigConstant.ROOT_NAME);
        formConfigTypeMapper.insert(formConfigTypeEntity);
        return formConfigTypeEntity;
    }

    /**
     * 保存或者更新节点的时候，同步更新名称路径
     */
    private String fillPathName(FormConfigType formConfigType) {
        AssertUtil.notNull(formConfigType.getName(), "分类名称不能为空");
        AssertUtil.notNull(formConfigType.getCompanyId(), "企业id不能为空");
        AssertUtil.notNull(formConfigType.getParentId(), "父级id不能为空");
        String pathName;
        List<FormConfigType> formConfigTypeList = queryByCompany(formConfigType.getCompanyId());
        // 拼接树型结构
        List<FormConfigTypeTreeNodeVO> formConfigTypeTreeNodeVOList = formConfigTypeList.stream()
                .map(parent -> new FormConfigTypeTreeNodeVO(
                        parent.getId().toString(), parent.getParentId().toString(),
                        parent.getName(), parent.getLevelType(), parent.getCompanyId()))
                .collect(Collectors.toList());
        List<FormConfigTypeTreeNodeVO> formConfigTypeTreeNodeList = TreeUtil.makeTree(formConfigTypeTreeNodeVOList);
        List<String> hitIds = new ArrayList<>();
        hitIds.add(formConfigType.getParentId().toString());
        List<FormConfigTypeTreeNodeVO> invokeTreeNodeList = TreeUtil.searchNode(formConfigTypeTreeNodeList, hitIds);
        if (CollectionUtils.isEmpty(invokeTreeNodeList)) {
            return formConfigType.getName();
        }
        FormConfigTypeTreeNodeVO rootFormConfigTypeTreeNodeVO = invokeTreeNodeList.get(0);
        pathName = TreeUtil.getParentTreeName(rootFormConfigTypeTreeNodeVO) + ">" + formConfigType.getName();
        return pathName;
    }

    private void checkFormConfigTypeNode(FormConfigType formConfigType) {
        LambdaQueryWrapper<FormConfigTypeEntity> queryWrapper = new LambdaQueryWrapper<FormConfigTypeEntity>()
                .eq(FormConfigTypeEntity::getLevelType, formConfigType.getLevelType())
                .eq(FormConfigTypeEntity::getParentId, formConfigType.getParentId())
                .ne(Objects.nonNull(formConfigType.getId()), FormConfigTypeEntity::getId, formConfigType.getId());
        List<FormConfigTypeEntity> formConfigTypeEntityList = formConfigTypeMapper.selectList(queryWrapper);
        for (FormConfigTypeEntity formConfigTypeEntity : formConfigTypeEntityList) {
            AssertUtil.isFalse(formConfigTypeEntity.getName().equals(formConfigType.getName()),
                    "分类名称已存在");
        }
    }
}
