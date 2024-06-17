package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.UpdateStatusParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.CustomConfigDetailReqDTO;
import cn.cuiot.dmp.base.infrastructure.feign.ArchiveFeignService;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.PageResult;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.system.application.param.dto.*;
import cn.cuiot.dmp.system.infrastructure.entity.UserHouseAuditEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.UserHouseAuditMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author caorui
 * @date 2024/6/13
 */
@Slf4j
@Service
public class UserHouseAuditService extends ServiceImpl<UserHouseAuditMapper, UserHouseAuditEntity> {

    @Autowired
    private SystemApiFeignService systemApiFeignService;

    @Autowired
    private ArchiveFeignService archiveFeignService;

    /**
     * 查询详情
     */
    public UserHouseAuditDTO queryForDetail(Long id) {
        UserHouseAuditEntity userHouseAuditEntity = Optional.ofNullable(getById(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        UserHouseAuditDTO userHouseAuditDTO = new UserHouseAuditDTO();
        BeanUtils.copyProperties(userHouseAuditEntity, userHouseAuditDTO);
        fillSystemOptionName(Lists.newArrayList(userHouseAuditDTO));
        return userHouseAuditDTO;
    }

    /**
     * 查询列表
     */
    public List<UserHouseAuditDTO> queryForList(UserHouseAuditPageQueryDTO queryDTO) {
        LambdaQueryWrapper<UserHouseAuditEntity> queryWrapper = new LambdaQueryWrapper<UserHouseAuditEntity>()
                .like(StringUtils.isNotBlank(queryDTO.getName()), UserHouseAuditEntity::getName, queryDTO.getName())
                .like(StringUtils.isNotBlank(queryDTO.getPhoneNumber()), UserHouseAuditEntity::getPhoneNumber, queryDTO.getPhoneNumber())
                .eq(Objects.nonNull(queryDTO.getAuditStatus()), UserHouseAuditEntity::getAuditStatus, queryDTO.getAuditStatus())
                .eq(Objects.nonNull(queryDTO.getUserId()), UserHouseAuditEntity::getUserId, queryDTO.getUserId())
                .eq(Objects.nonNull(queryDTO.getBuildingId()), UserHouseAuditEntity::getBuildingId, queryDTO.getBuildingId())
                .ge(Objects.nonNull(queryDTO.getBeginTime()), UserHouseAuditEntity::getCreatedOn, queryDTO.getBeginTime())
                .le(Objects.nonNull(queryDTO.getEndTime()), UserHouseAuditEntity::getCreatedOn, queryDTO.getEndTime())
                .orderByDesc(UserHouseAuditEntity::getCreatedOn);
        List<UserHouseAuditEntity> userHouseAuditEntityList = list(queryWrapper);
        if (CollectionUtils.isEmpty(userHouseAuditEntityList)) {
            return new ArrayList<>();
        }
        List<UserHouseAuditDTO> userHouseAuditDTOList = userHouseAuditEntityList.stream()
                .map(o -> {
                    UserHouseAuditDTO userHouseAuditDTO = new UserHouseAuditDTO();
                    BeanUtils.copyProperties(o, userHouseAuditDTO);
                    return userHouseAuditDTO;
                })
                .collect(Collectors.toList());
        fillSystemOptionName(userHouseAuditDTOList);
        return userHouseAuditDTOList;
    }

    /**
     * 查询分页列表
     */
    public PageResult<UserHouseAuditDTO> queryForPage(UserHouseAuditPageQueryDTO queryDTO) {
        LambdaQueryWrapper<UserHouseAuditEntity> queryWrapper = new LambdaQueryWrapper<UserHouseAuditEntity>()
                .like(StringUtils.isNotBlank(queryDTO.getName()), UserHouseAuditEntity::getName, queryDTO.getName())
                .like(StringUtils.isNotBlank(queryDTO.getPhoneNumber()), UserHouseAuditEntity::getPhoneNumber, queryDTO.getPhoneNumber())
                .eq(Objects.nonNull(queryDTO.getAuditStatus()), UserHouseAuditEntity::getAuditStatus, queryDTO.getAuditStatus())
                .eq(Objects.nonNull(queryDTO.getUserId()), UserHouseAuditEntity::getUserId, queryDTO.getUserId())
                .eq(Objects.nonNull(queryDTO.getBuildingId()), UserHouseAuditEntity::getBuildingId, queryDTO.getBuildingId())
                .ge(Objects.nonNull(queryDTO.getBeginTime()), UserHouseAuditEntity::getCreatedOn, queryDTO.getBeginTime())
                .le(Objects.nonNull(queryDTO.getEndTime()), UserHouseAuditEntity::getCreatedOn, queryDTO.getEndTime())
                .orderByDesc(UserHouseAuditEntity::getCreatedOn);
        IPage<UserHouseAuditEntity> userHouseAuditEntityIPage = page(new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize()), queryWrapper);
        if (CollectionUtils.isEmpty(userHouseAuditEntityIPage.getRecords())) {
            return new PageResult<>();
        }
        return userHouseAuditEntity2UserHouseAuditDTOs(userHouseAuditEntityIPage);
    }

    /**
     * 根据用户id查询楼盘列表
     */
    public List<UserHouseBuildingDTO> queryBuildingsByUser(Long userId) {
        AssertUtil.notNull(userId, "用户id不能为空");
        LambdaQueryWrapper<UserHouseAuditEntity> queryWrapper = new LambdaQueryWrapper<UserHouseAuditEntity>()
                .eq(UserHouseAuditEntity::getUserId, userId);
        List<UserHouseAuditEntity> userHouseAuditEntityList = list(queryWrapper);
        if (CollectionUtils.isEmpty(userHouseAuditEntityList)) {
            return new ArrayList<>();
        }
        Set<Long> buildingIdList = userHouseAuditEntityList.stream()
                .map(UserHouseAuditEntity::getBuildingId)
                .collect(Collectors.toSet());
        return buildingIdList.stream()
                .map(o->{
                    UserHouseBuildingDTO userHouseBuildingDTO = new UserHouseBuildingDTO();
                    IdParam idParam = new IdParam();
                    idParam.setId(o);
                    BuildingArchive buildingArchive = archiveFeignService.lookupBuildingArchiveInfo(idParam).getData();
                    userHouseBuildingDTO.setUserId(userId);
                    userHouseBuildingDTO.setBuildingId(o);
                    userHouseBuildingDTO.setBuildingName(buildingArchive.getName());
                    return userHouseBuildingDTO;
                })
                .collect(Collectors.toList());
    }

    /**
     * 保存
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveUserHouseAudit(UserHouseAuditCreateDTO createDTO) {
        UserHouseAuditEntity userHouseAuditEntity = new UserHouseAuditEntity();
        BeanUtils.copyProperties(createDTO, userHouseAuditEntity);
        return save(userHouseAuditEntity);
    }

    /**
     * 更新
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserHouseAudit(UserHouseAuditUpdateDTO updateDTO) {
        UserHouseAuditEntity userHouseAuditEntity = Optional.ofNullable(getById(updateDTO.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BeanUtils.copyProperties(updateDTO, userHouseAuditEntity);
        return updateById(userHouseAuditEntity);
    }

    /**
     * 审核
     */
    public boolean updateAuditStatus(UserHouseAuditStatusDTO statusDTO) {
        UserHouseAuditEntity userHouseAuditEntity = Optional.ofNullable(getById(statusDTO.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BeanUtils.copyProperties(statusDTO, userHouseAuditEntity);
        return updateById(userHouseAuditEntity);
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserHouseAudit(Long id) {
        return removeById(id);
    }

    private PageResult<UserHouseAuditDTO> userHouseAuditEntity2UserHouseAuditDTOs(IPage<UserHouseAuditEntity> userHouseAuditEntityIPage) {
        PageResult<UserHouseAuditDTO> userHouseAuditDTOPageResult = new PageResult<>();
        List<UserHouseAuditDTO> UserHouseAuditDTOList = userHouseAuditEntityIPage.getRecords().stream()
                .map(o -> {
                    UserHouseAuditDTO userHouseAuditDTO = new UserHouseAuditDTO();
                    BeanUtils.copyProperties(o, userHouseAuditDTO);
                    return userHouseAuditDTO;
                })
                .collect(Collectors.toList());
        fillSystemOptionName(UserHouseAuditDTOList);
        userHouseAuditDTOPageResult.setList(UserHouseAuditDTOList);
        userHouseAuditDTOPageResult.setCurrentPage((int) userHouseAuditEntityIPage.getCurrent());
        userHouseAuditDTOPageResult.setPageSize((int) userHouseAuditEntityIPage.getSize());
        userHouseAuditDTOPageResult.setTotal(userHouseAuditEntityIPage.getTotal());
        return userHouseAuditDTOPageResult;
    }

    /**
     * 使用配置id列表查询出，对应的名称关系
     *
     * @param userHouseAuditDTOList 线索列表
     */
    private void fillSystemOptionName(List<UserHouseAuditDTO> userHouseAuditDTOList) {
        if (CollectionUtils.isEmpty(userHouseAuditDTOList)) {
            return;
        }
        Set<Long> configIdList = new HashSet<>();
        userHouseAuditDTOList.forEach(o -> {
            if (Objects.nonNull(o.getCardTypeId())) {
                configIdList.add(o.getCardTypeId());
            }
        });
        CustomConfigDetailReqDTO customConfigDetailReqDTO = new CustomConfigDetailReqDTO();
        customConfigDetailReqDTO.setCustomConfigDetailIdList(new ArrayList<>(configIdList));
        Map<Long, String> systemOptionMap = systemApiFeignService.batchQueryCustomConfigDetailsForMap(customConfigDetailReqDTO)
                .getData();
        userHouseAuditDTOList.forEach(o -> {
            if (Objects.nonNull(o.getCardTypeId()) && systemOptionMap.containsKey(o.getCardTypeId())) {
                o.setCardTypeIdName(systemOptionMap.get(o.getCardTypeId()));
            }
        });
    }

}
