package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.infrastructure.dto.IdParam;
import cn.cuiot.dmp.base.infrastructure.dto.req.CustomConfigDetailReqDTO;
import cn.cuiot.dmp.base.infrastructure.feign.ArchiveFeignService;
import cn.cuiot.dmp.base.infrastructure.feign.SystemApiFeignService;
import cn.cuiot.dmp.base.infrastructure.model.BuildingArchive;
import cn.cuiot.dmp.common.constant.EntityConstants;
import cn.cuiot.dmp.common.constant.NumberConst;
import cn.cuiot.dmp.common.constant.ResultCode;
import cn.cuiot.dmp.common.constant.UserHouseAuditStatusConstants;
import cn.cuiot.dmp.common.exception.BusinessException;
import cn.cuiot.dmp.common.utils.AssertUtil;
import cn.cuiot.dmp.common.utils.Sm4;
import cn.cuiot.dmp.system.application.param.dto.UserHouseAuditCreateDTO;
import cn.cuiot.dmp.system.application.param.dto.UserHouseAuditDTO;
import cn.cuiot.dmp.system.application.param.dto.UserHouseAuditPageQueryDTO;
import cn.cuiot.dmp.system.application.param.dto.UserHouseAuditStatusDTO;
import cn.cuiot.dmp.system.application.param.dto.UserHouseAuditUpdateDTO;
import cn.cuiot.dmp.system.application.param.dto.UserHouseBuildingDTO;
import cn.cuiot.dmp.system.infrastructure.entity.UserHouseAuditEntity;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.UserHouseAuditMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.sun.org.apache.regexp.internal.RE;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Autowired
    private UserHouseAuditMapper userHouseAuditMapper;

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

        List<UserHouseAuditDTO> selectList = userHouseAuditMapper.queryForList(queryDTO);
        if (CollectionUtils.isEmpty(selectList)) {
            return new ArrayList<>();
        }
        List<UserHouseAuditDTO> userHouseAuditDTOList = selectList;
        fillSystemOptionName(userHouseAuditDTOList);
        return userHouseAuditDTOList;
    }

    /**
     * 查询分页列表
     */
    public IPage<UserHouseAuditDTO> queryForPage(UserHouseAuditPageQueryDTO queryDTO) {
        IPage<UserHouseAuditDTO> page = userHouseAuditMapper
                .queryForList(new Page<>(queryDTO.getPageNo(), queryDTO.getPageSize()), queryDTO);
        if (CollectionUtils.isNotEmpty(page.getRecords())) {
            fillSystemOptionName(page.getRecords());
        }
        return page;
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
                .map(o -> {
                    UserHouseBuildingDTO userHouseBuildingDTO = new UserHouseBuildingDTO();
                    IdParam idParam = new IdParam();
                    idParam.setId(o);
                    BuildingArchive buildingArchive = archiveFeignService
                            .lookupBuildingArchiveInfo(idParam).getData();
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
        if (StringUtils.isNotBlank(createDTO.getPhoneNumber())) {
            createDTO.setPhoneNumber(Sm4.encryption(createDTO.getPhoneNumber()));
        }
        if (StringUtils.isNotBlank(createDTO.getIdentityNum())) {
            createDTO.setIdentityNum(Sm4.encryption(createDTO.getIdentityNum()));
        }
        UserHouseAuditEntity userHouseAuditEntity = new UserHouseAuditEntity();
        BeanUtils.copyProperties(createDTO, userHouseAuditEntity);
        return save(userHouseAuditEntity);
    }

    /**
     * 更新
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateUserHouseAudit(UserHouseAuditUpdateDTO updateDTO) {
        if (StringUtils.isNotBlank(updateDTO.getPhoneNumber())) {
            updateDTO.setPhoneNumber(Sm4.encryption(updateDTO.getPhoneNumber()));
        }
        if (StringUtils.isNotBlank(updateDTO.getIdentityNum())) {
            updateDTO.setIdentityNum(Sm4.encryption(updateDTO.getIdentityNum()));
        }
        UserHouseAuditEntity userHouseAuditEntity = Optional.ofNullable(getById(updateDTO.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        BeanUtils.copyProperties(updateDTO, userHouseAuditEntity);
        return updateById(userHouseAuditEntity);
    }

    /**
     * 审核
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean updateAuditStatus(UserHouseAuditStatusDTO statusDTO) {

        UserHouseAuditEntity userHouseAuditEntity = Optional.ofNullable(getById(statusDTO.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));

        if(UserHouseAuditStatusConstants.PASS.equals(statusDTO.getAuditStatus())){
            //绑定客户本人
            if(Objects.equals(NumberConst.ONE.byteValue(),statusDTO.getBindCustomerType())){
                if(alreadyBindOtherUser(userHouseAuditEntity.getUserId(),statusDTO.getBindCustomerId(),null)){
                    throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"所选客户已绑定其他用户账号");
                }
            }
            //客户家庭成员
            if(Objects.equals(NumberConst.TWO.byteValue(),statusDTO.getBindCustomerType())){
                if(alreadyBindOtherUser(userHouseAuditEntity.getUserId(),statusDTO.getBindCustomerId(),statusDTO.getBindCustomerMemberId())){
                    throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT,"所选成员已绑定其他用户账号");
                }
            }
        }

        LambdaUpdateWrapper<UserHouseAuditEntity> updateWrapper = Wrappers.lambdaUpdate();

        updateWrapper.set(UserHouseAuditEntity::getAuditStatus, statusDTO.getAuditStatus());
        updateWrapper.set(UserHouseAuditEntity::getRejectReason, statusDTO.getRejectReason());
        updateWrapper.set(UserHouseAuditEntity::getBindCustomerId, statusDTO.getBindCustomerId());
        updateWrapper.set(UserHouseAuditEntity::getBindCustomerType, statusDTO.getBindCustomerType());
        updateWrapper.set(UserHouseAuditEntity::getBindCustomerMemberId,
                statusDTO.getBindCustomerMemberId());

        updateWrapper.eq(UserHouseAuditEntity::getId, userHouseAuditEntity.getId());
        return super.update(null, updateWrapper);
    }

    /**
     * 判断是否绑定其他用户
     * @param userId
     * @param bindCustomerId
     * @return
     */
    private Boolean alreadyBindOtherUser(Long userId,Long bindCustomerId,Long bindCustomerMemberId){
        LambdaQueryWrapper<UserHouseAuditEntity> lambdaQueryWrapper = Wrappers.<UserHouseAuditEntity>lambdaQuery()
                .eq(UserHouseAuditEntity::getBindCustomerId, bindCustomerId)
                .eq(UserHouseAuditEntity::getAuditStatus, UserHouseAuditStatusConstants.PASS);
        if(Objects.nonNull(bindCustomerMemberId)){
            lambdaQueryWrapper.ne(UserHouseAuditEntity::getBindCustomerMemberId,bindCustomerMemberId);
        }
        if(Objects.nonNull(userId)){
            lambdaQueryWrapper.ne(UserHouseAuditEntity::getUserId,userId);
        }
        List<UserHouseAuditEntity> selectList = userHouseAuditMapper
                .selectList(lambdaQueryWrapper);
        if(CollectionUtils.isNotEmpty(selectList)){
            return true;
        }
        return false;
    }

    /**
     * 删除
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean deleteUserHouseAudit(Long id) {
        LambdaUpdateWrapper<UserHouseAuditEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(UserHouseAuditEntity::getDeletedFlag, EntityConstants.DELETED);
        updateWrapper.eq(UserHouseAuditEntity::getId, id);
        return super.update(null, updateWrapper);
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

        userHouseAuditDTOList.forEach(item -> {
            if (StringUtils.isNotBlank(item.getPhoneNumber())) {
                item.setPhoneNumber(Sm4.decrypt(item.getPhoneNumber()));
            }
            if (StringUtils.isNotBlank(item.getIdentityNum())) {
                item.setIdentityNum(Sm4.decrypt(item.getIdentityNum()));
            }
        });

        Set<Long> configIdList = new HashSet<>();
        userHouseAuditDTOList.forEach(o -> {
            if (Objects.nonNull(o.getCardTypeId())) {
                configIdList.add(o.getCardTypeId());
            }
        });
        CustomConfigDetailReqDTO customConfigDetailReqDTO = new CustomConfigDetailReqDTO();
        customConfigDetailReqDTO.setCustomConfigDetailIdList(new ArrayList<>(configIdList));
        Map<Long, String> systemOptionMap = systemApiFeignService
                .batchQueryCustomConfigDetailsForMap(customConfigDetailReqDTO)
                .getData();
        userHouseAuditDTOList.forEach(o -> {
            if (Objects.nonNull(o.getCardTypeId()) && systemOptionMap
                    .containsKey(o.getCardTypeId())) {
                o.setCardTypeIdName(systemOptionMap.get(o.getCardTypeId()));
            }
        });
    }

    /**
     * 取消身份
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean cancelAuditStatus(IdParam idParam) {
        UserHouseAuditEntity userHouseAuditEntity = Optional.ofNullable(getById(idParam.getId()))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));
        LambdaUpdateWrapper<UserHouseAuditEntity> updateWrapper = Wrappers.lambdaUpdate();
        updateWrapper.set(UserHouseAuditEntity::getAuditStatus,
                UserHouseAuditStatusConstants.INVALID);
        updateWrapper.eq(UserHouseAuditEntity::getId, userHouseAuditEntity.getId());
        return super.update(null, updateWrapper);
    }
}
