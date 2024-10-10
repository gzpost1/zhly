package cn.cuiot.dmp.system.application.service;

import cn.cuiot.dmp.base.application.dto.ExcelReportDto;
import cn.cuiot.dmp.base.application.service.ExcelExportService;
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
import cn.cuiot.dmp.domain.types.LoginInfoHolder;
import cn.cuiot.dmp.system.application.param.dto.*;
import cn.cuiot.dmp.system.application.param.vo.export.UserHouseAuditExportVo;
import cn.cuiot.dmp.system.infrastructure.entity.UserHouseAuditEntity;
import cn.cuiot.dmp.system.infrastructure.entity.dto.UserResDTO;
import cn.cuiot.dmp.system.infrastructure.persistence.mapper.UserHouseAuditMapper;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
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

    @Autowired
    private UserHouseAuditMapper userHouseAuditMapper;

    @Autowired
    private UserService userService;
    @Autowired
    private ExcelExportService excelExportService;

    /**
     * 查询详情
     */
    public UserHouseAuditDTO queryForDetail(Long id) {

        UserHouseAuditDTO userHouseAuditDTO = Optional.ofNullable(userHouseAuditMapper.queryForDetail(id))
                .orElseThrow(() -> new BusinessException(ResultCode.OBJECT_NOT_EXIST));

        fillSystemOptionName(Lists.newArrayList(userHouseAuditDTO));

        UserResDTO userById = userService.getUserById(userHouseAuditDTO.getUserId().toString());
        if (null != userById) {
            if (StringUtils.isNotBlank(userById.getPhoneNumber())) {
                userById.setPhoneNumber(Sm4.decrypt(userById.getPhoneNumber()));
            }
        }
        userHouseAuditDTO.setUser(userById);

        return userHouseAuditDTO;
    }

    /**
     * 查询列表
     */
    public List<UserHouseAuditDTO> queryForList(UserHouseAuditPageQueryDTO queryDTO) {
        if (StringUtils.isNotBlank(queryDTO.getPhoneNumber())) {
            queryDTO.setPhoneNumber(Sm4.encryption(queryDTO.getPhoneNumber()));
        }
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
        if (StringUtils.isNotBlank(queryDTO.getPhoneNumber())) {
            queryDTO.setPhoneNumber(Sm4.encryption(queryDTO.getPhoneNumber()));
        }
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
                    userHouseBuildingDTO.setCompanyId(buildingArchive.getCompanyId());
                    userHouseBuildingDTO.setDepartmentId(buildingArchive.getDepartmentId());
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

        if (UserHouseAuditStatusConstants.PASS.equals(statusDTO.getAuditStatus())) {
            //绑定客户本人
            if (Objects.equals(NumberConst.ONE.byteValue(), statusDTO.getBindCustomerType())) {
                if (alreadyBindOtherUser(userHouseAuditEntity.getUserId(), statusDTO.getBindCustomerId(), null)) {
                    throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "所选客户已绑定其他用户账号");
                }
            }
            //客户家庭成员
            if (Objects.equals(NumberConst.TWO.byteValue(), statusDTO.getBindCustomerType())) {
                if (alreadyBindOtherUser(userHouseAuditEntity.getUserId(), statusDTO.getBindCustomerId(), statusDTO.getBindCustomerMemberId())) {
                    throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "所选成员已绑定其他用户账号");
                }
            }
        }

        LambdaUpdateWrapper<UserHouseAuditEntity> updateWrapper = Wrappers.lambdaUpdate();

        updateWrapper.set(UserHouseAuditEntity::getAuditTime, new Date());
        updateWrapper.set(UserHouseAuditEntity::getAuditUserId, LoginInfoHolder.getCurrentUserId());
        updateWrapper.set(UserHouseAuditEntity::getAuditPerson, LoginInfoHolder.getCurrentName());

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
     *
     * @param userId
     * @param bindCustomerId
     * @return
     */
    private Boolean alreadyBindOtherUser(Long userId, Long bindCustomerId, Long bindCustomerMemberId) {
        LambdaQueryWrapper<UserHouseAuditEntity> lambdaQueryWrapper = Wrappers.<UserHouseAuditEntity>lambdaQuery()
                .eq(UserHouseAuditEntity::getBindCustomerId, bindCustomerId)
                .eq(UserHouseAuditEntity::getAuditStatus, UserHouseAuditStatusConstants.PASS);
        if (Objects.nonNull(bindCustomerMemberId)) {
            lambdaQueryWrapper.ne(UserHouseAuditEntity::getBindCustomerMemberId, bindCustomerMemberId);
        }
        if (Objects.nonNull(userId)) {
            lambdaQueryWrapper.ne(UserHouseAuditEntity::getUserId, userId);
        }
        List<UserHouseAuditEntity> selectList = userHouseAuditMapper
                .selectList(lambdaQueryWrapper);
        if (CollectionUtils.isNotEmpty(selectList)) {
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

    /**
     * 根据楼盘id列表查询对应的业主
     */
    public Map<Long, List<Long>> lookUpUserIdsByBuildingIds(List<Long> buildingIds) {
        AssertUtil.notEmpty(buildingIds, "楼盘编码列表不能为空");
        LambdaQueryWrapper<UserHouseAuditEntity> lambdaQueryWrapper = Wrappers.<UserHouseAuditEntity>lambdaQuery()
                .in(UserHouseAuditEntity::getBuildingId, buildingIds)
                .eq(UserHouseAuditEntity::getAuditStatus, UserHouseAuditStatusConstants.PASS);
        List<UserHouseAuditEntity> userHouseAuditEntities = list(lambdaQueryWrapper);
        if (CollectionUtils.isEmpty(userHouseAuditEntities)) {
            return new HashMap<>();
        }
        Map<Long, List<UserHouseAuditEntity>> buildingUserEntityMap = userHouseAuditEntities.stream()
                .collect(Collectors.groupingBy(UserHouseAuditEntity::getBuildingId));
        Map<Long, List<Long>> buildingUserMap = new HashMap<>();
        buildingUserEntityMap.forEach((k, v) -> {
            List<Long> userIdList = v.stream()
                    .map(UserHouseAuditEntity::getUserId)
                    .collect(Collectors.toList());
            buildingUserMap.put(k, userIdList);
        });
        return buildingUserMap;
    }

    public void export(UserHouseAuditPageQueryDTO pageQuery) throws Exception {
        IPage<UserHouseAuditDTO> pageResult = new Page<>();
        Long pageNo = 1L;
        pageQuery.setPageSize(2000L);
        List<UserHouseAuditExportVo> exportDataList = new ArrayList<>();
        do {
            pageQuery.setPageNo(pageNo++);
            pageResult = this.queryForPage(pageQuery);
            if (pageResult.getTotal() > ExcelExportService.MAX_EXPORT_DATA) {
                throw new BusinessException(ResultCode.EXPORT_DATA_OVER_LIMIT);
            }
            pageResult.getRecords().forEach(o -> {
                UserHouseAuditExportVo exportVo = new UserHouseAuditExportVo();
                BeanUtils.copyProperties(o, exportVo);
                exportDataList.add(exportVo);
            });
        } while (CollUtil.isNotEmpty(pageResult.getRecords()));
        ExcelReportDto<UserHouseAuditPageQueryDTO, UserHouseAuditExportVo> excelReportDto = null;
        if (UserHouseAuditStatusConstants.WAIT.equals(pageQuery.getAuditStatus())) {
            excelReportDto = ExcelReportDto.<UserHouseAuditPageQueryDTO, UserHouseAuditExportVo>builder().title("待审核C端用户").fileName("待审核C端用户导出").SheetName("待审核C端用户").dataList(exportDataList).build();
        } else if (UserHouseAuditStatusConstants.PASS.equals(pageQuery.getAuditStatus())) {
            excelReportDto = ExcelReportDto.<UserHouseAuditPageQueryDTO, UserHouseAuditExportVo>builder().title("审核通过C端用户").fileName("审核通过C端用户导出").SheetName("审核通过C端用户").dataList(exportDataList).build();
        } else if (UserHouseAuditStatusConstants.REJECT.equals(pageQuery.getAuditStatus())) {
            excelReportDto = ExcelReportDto.<UserHouseAuditPageQueryDTO, UserHouseAuditExportVo>builder().title("审核驳回C端用户").fileName("审核驳回C端用户导出").SheetName("审核驳回C端用户").dataList(exportDataList).build();
        } else {
            throw new BusinessException(ResultCode.PARAM_NOT_COMPLIANT, "传入的审核状态不对");
        }
        excelExportService.excelExport(excelReportDto, UserHouseAuditExportVo.class);
    }
}
