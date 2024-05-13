package cn.cuiot.dmp.system.infrastructure.domain.repository.impl;

import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.system.application.constant.CommonOptionConstant;
import cn.cuiot.dmp.system.domain.aggregate.CommonOption;
import cn.cuiot.dmp.system.domain.aggregate.CommonOptionPageQuery;
import cn.cuiot.dmp.system.domain.repository.CommonOptionRepository;
import cn.cuiot.dmp.system.infrastructure.entity.CommonOptionEntity;
import cn.cuiot.dmp.system.infrastructure.entity.CommonOptionDetailEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.CommonOptionDetailQueryDTO;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.CommonOptionMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
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
public class CommonOptionRepositoryImpl implements CommonOptionRepository {

    @Autowired
    private CommonOptionMapper commonOptionMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public CommonOption queryForDetail(Long id) {
        CommonOptionEntity commonOptionEntity = Optional.ofNullable(commonOptionMapper.selectById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        CommonOption commonOption = new CommonOption();
        BeanUtils.copyProperties(commonOptionEntity, commonOption);
        // 查询常用选项详情
        CommonOptionDetailEntity commonOptionDetailEntity = mongoTemplate.findById(commonOption.getId(),
                CommonOptionDetailEntity.class, CommonOptionConstant.COMMON_OPTION_COLLECTION);
        if (Objects.nonNull(commonOptionDetailEntity)) {
            commonOption.setCommonOptionDetail(commonOptionDetailEntity.getCommonOptionDetail());
        }
        return commonOption;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int saveCommonOption(CommonOption commonOption) {
        commonOption.setId(IdWorker.getId());
        CommonOptionEntity commonOptionEntity = new CommonOptionEntity();
        BeanUtils.copyProperties(commonOption, commonOptionEntity);
        // 保存常用选项内容
        CommonOptionDetailEntity commonOptionDetailEntity = new CommonOptionDetailEntity();
        BeanUtils.copyProperties(commonOption, commonOptionDetailEntity);
        mongoTemplate.save(commonOptionDetailEntity, CommonOptionConstant.COMMON_OPTION_COLLECTION);
        return commonOptionMapper.insert(commonOptionEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCommonOption(CommonOption commonOption) {
        CommonOptionEntity commonOptionEntity = Optional.ofNullable(commonOptionMapper.selectById(commonOption.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BeanUtils.copyProperties(commonOption, commonOptionEntity);
        // 先删除后保存常用选项内容
        CommonOptionDetailQueryDTO commonOptionDetailQueryDTO = new CommonOptionDetailQueryDTO();
        commonOptionDetailQueryDTO.setId(commonOption.getId());
        Query query = getQuery(commonOptionDetailQueryDTO);
        mongoTemplate.remove(query, CommonOptionConstant.COMMON_OPTION_COLLECTION);
        CommonOptionDetailEntity commonOptionDetailEntity = new CommonOptionDetailEntity();
        BeanUtils.copyProperties(commonOption, commonOptionDetailEntity);
        mongoTemplate.save(commonOptionDetailEntity, CommonOptionConstant.COMMON_OPTION_COLLECTION);
        return commonOptionMapper.updateById(commonOptionEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateCommonOptionStatus(CommonOption commonOption) {
        CommonOptionEntity commonOptionEntity = Optional.ofNullable(commonOptionMapper.selectById(commonOption.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        commonOptionEntity.setStatus(commonOption.getStatus());
        return commonOptionMapper.updateById(commonOptionEntity);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteCommonOption(Long id) {
        // 先删除常用选项详情，后删除常用选项
        CommonOptionDetailQueryDTO commonOptionDetailQueryDTO = new CommonOptionDetailQueryDTO();
        commonOptionDetailQueryDTO.setId(id);
        Query query = getQuery(commonOptionDetailQueryDTO);
        mongoTemplate.remove(query, CommonOptionConstant.COMMON_OPTION_COLLECTION);
        return commonOptionMapper.deleteById(id);
    }

    @Override
    public List<CommonOption> batchQueryCommonOption(Byte status, List<Long> idList) {
        AssertUtil.notEmpty(idList, "常用选项ID列表不能为空");
        // 获取常用选项详情列表
        CommonOptionDetailQueryDTO commonOptionDetailQueryDTO = new CommonOptionDetailQueryDTO();
        commonOptionDetailQueryDTO.setIdList(idList);
        Query query = getQuery(commonOptionDetailQueryDTO);
        List<CommonOptionDetailEntity> commonOptionDetailEntityList = mongoTemplate.find(query,
                CommonOptionDetailEntity.class, CommonOptionConstant.COMMON_OPTION_COLLECTION);
        // 获取常用选项
        LambdaQueryWrapper<CommonOptionEntity> queryWrapper = new LambdaQueryWrapper<CommonOptionEntity>()
                .in(CommonOptionEntity::getId, idList)
                .eq(Objects.nonNull(status), CommonOptionEntity::getStatus, status);
        List<CommonOptionEntity> commonOptionEntityList = commonOptionMapper.selectList(queryWrapper);
        if (CollectionUtils.isEmpty(commonOptionEntityList)) {
            return new ArrayList<>();
        }
        return commonOptionEntity2CommonOption(commonOptionEntityList, commonOptionDetailEntityList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchMoveCommonOption(Long typeId, List<Long> idList) {
        return commonOptionMapper.batchUpdateCommonOptionById(typeId, null, idList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchMoveCommonOptionDefault(List<String> typeIdList) {
        return commonOptionMapper.batchMoveCommonOptionDefault(typeIdList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchUpdateCommonOptionStatus(Byte status, List<Long> idList) {
        return commonOptionMapper.batchUpdateCommonOptionById(null, status, idList);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int batchDeleteCommonOption(List<Long> idList) {
        // 先删除常用选项详情，后删除常用选项
        CommonOptionDetailQueryDTO commonOptionDetailQueryDTO = new CommonOptionDetailQueryDTO();
        commonOptionDetailQueryDTO.setIdList(idList);
        Query query = getQuery(commonOptionDetailQueryDTO);
        mongoTemplate.remove(query, CommonOptionConstant.COMMON_OPTION_COLLECTION);
        return commonOptionMapper.deleteBatchIds(idList);
    }

    @Override
    public PageResult<CommonOption> queryCommonOptionByType(CommonOptionPageQuery pageQuery) {
        LambdaQueryWrapper<CommonOptionEntity> queryWrapper = new LambdaQueryWrapper<CommonOptionEntity>()
                .eq(Objects.nonNull(pageQuery.getTypeId()), CommonOptionEntity::getTypeId, pageQuery.getTypeId())
                .like(StringUtils.isNotBlank(pageQuery.getName()), CommonOptionEntity::getName, pageQuery.getName())
                .eq(Objects.nonNull(pageQuery.getStatus()), CommonOptionEntity::getStatus, pageQuery.getStatus());
        IPage<CommonOptionEntity> commonOptionEntityPage = commonOptionMapper.selectPage(
                new Page<>(pageQuery.getPageNo(), pageQuery.getPageSize()), queryWrapper);
        if (Objects.isNull(commonOptionEntityPage) || CollectionUtils.isEmpty(commonOptionEntityPage.getRecords())) {
            return new PageResult<>();
        }
        return commonOptionEntity2CommonOption(commonOptionEntityPage);
    }

    private Query getQuery(CommonOptionDetailQueryDTO commonOptionDetailQueryDTO) {
        Query query = new Query();
        Criteria criteria = new Criteria();
        if (Objects.nonNull(commonOptionDetailQueryDTO.getId())) {
            criteria.and(CommonOptionConstant.COMMON_OPTION_COLLECTION_PK).is(commonOptionDetailQueryDTO.getId());
        }
        if (CollectionUtils.isNotEmpty(commonOptionDetailQueryDTO.getIdList())) {
            criteria.and(CommonOptionConstant.COMMON_OPTION_COLLECTION_PK).in(commonOptionDetailQueryDTO.getIdList());
        }
        query.addCriteria(criteria);
        return query;
    }

    private PageResult<CommonOption> commonOptionEntity2CommonOption(IPage<CommonOptionEntity> commonOptionEntityPage) {
        PageResult<CommonOption> commonOptionPageResult = new PageResult<>();
        List<CommonOption> commonOptionList = commonOptionEntityPage.getRecords().stream()
                .map(o -> {
                    CommonOption commonOption = new CommonOption();
                    BeanUtils.copyProperties(o, commonOption);
                    return commonOption;
                })
                .collect(Collectors.toList());
        commonOptionPageResult.setList(commonOptionList);
        commonOptionPageResult.setCurrentPage((int) commonOptionEntityPage.getCurrent());
        commonOptionPageResult.setPageSize((int) commonOptionEntityPage.getSize());
        commonOptionPageResult.setTotal(commonOptionEntityPage.getTotal());
        return commonOptionPageResult;
    }

    private List<CommonOption> commonOptionEntity2CommonOption(List<CommonOptionEntity> commonOptionEntityList,
                                                         List<CommonOptionDetailEntity> commonOptionDetailEntityList) {
        List<CommonOption> commonOptionList = new ArrayList<>();
        for (CommonOptionEntity commonOptionEntity : commonOptionEntityList) {
            CommonOption commonOption = new CommonOption();
            BeanUtils.copyProperties(commonOptionEntity, commonOption);
            for (CommonOptionDetailEntity commonOptionDetailEntity : commonOptionDetailEntityList) {
                if (commonOptionDetailEntity.getId().equals(commonOptionEntity.getId())) {
                    commonOption.setCommonOptionDetail(commonOptionDetailEntity.getCommonOptionDetail());
                    break;
                }
            }
            commonOptionList.add(commonOption);
        }
        return commonOptionList;
    }
    
}
